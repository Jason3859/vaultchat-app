package dev.jason.app.compose.vaultchat.feature.messaging.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.model.message.Message
import dev.jason.app.compose.vaultchat.core.model.message.MessageUi
import dev.jason.app.compose.vaultchat.core.model.message.toUi
import dev.jason.app.compose.vaultchat.core.model.user.UserUi
import dev.jason.app.compose.vaultchat.core.model.user.toUser
import dev.jason.app.compose.vaultchat.core.utils.OpenLinksService
import dev.jason.app.compose.vaultchat.feature.messaging.domain.MessageDatabaseRepository
import dev.jason.app.compose.vaultchat.feature.messaging.domain.MessagingApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class MessagingViewModel(
    otherUser: UserUi,
    private val messageDatabaseRepository: MessageDatabaseRepository,
    private val messagingApiService: MessagingApiRepository,
    private val openLinksService: OpenLinksService
) : ViewModel() {

    private val _uiState = MutableStateFlow(MessagingUiState())
    val uiState = _uiState.asStateFlow()

    private val currentUser by lazy { AppState.currentUser.value!! }

    private val _otherUser = MutableStateFlow<UserUi?>(null)
    val otherUser = _otherUser.asStateFlow()

    private val _messages = MutableStateFlow<PagingData<MessageUi>>(PagingData.Companion.empty())
    val messages = _messages.asStateFlow()

    private val _pendingMessages = mutableStateListOf<MessageUi>()
    val pendingMessages: List<MessageUi> = _pendingMessages

    private val _failedMessages = mutableStateListOf<MessageUi>()
    val failedMessages: List<MessageUi> = _failedMessages

    fun onAction(action: MessagingUiAction) {
        when (action) {
            is MessagingUiAction.UpdateState -> updateState(action.state)
            is MessagingUiAction.SendMessage -> sendMessage()
            is MessagingUiAction.OpenLink -> openLink(action.url)
        }
    }

    private fun updateState(state: (MessagingUiState) -> MessagingUiState) {
        _uiState.update {
            state(it).let { newState ->
                newState.copy(
                    messageText = newState.messageText.trim(),
                    sendButtonEnabled = newState.messageText.isNotBlank()
                )
            }
        }
    }

    private fun sendMessage() {
        val uiState = _uiState.value.copy()
        val otherUser = _otherUser.value!!

        updateState { currentState ->
            currentState.copy(
                messageText = ""
            )
        }

        viewModelScope.launch {
            val message = Message(
                from = currentUser,
                to = otherUser.toUser(),
                text = uiState.messageText,
                timestamp = LocalDateTime.now()
            )
            val messageUi = message.toUi()
            _pendingMessages.add(messageUi)

            messagingApiService.sendMessage(message)
                .onSuccess {
                    _pendingMessages.remove(messageUi)
                }
        }
    }

    private fun openLink(url: String) {
        openLinksService.open(url)
    }

    init {
        viewModelScope.launch {
            _otherUser.update { otherUser }
            AppState.updateOtherUser(otherUser.toUser())
        }

        viewModelScope.launch {
            messageDatabaseRepository.getMessagesPaginated(
                currentUserUid = currentUser.uid,
                otherUserUid = otherUser.uid,
                coroutineScope = this
            )
                .map { it.map(Message::toUi) }
                .cachedIn(this)
                .collect { data ->
                    _messages.value = data
                }
        }

        viewModelScope.launch {
            AppState.isOtherUserBlocked.collect {
                _uiState.update { state ->
                    state.copy(
                        isUserBlocked = it
                    )
                }
            }
        }
    }
}