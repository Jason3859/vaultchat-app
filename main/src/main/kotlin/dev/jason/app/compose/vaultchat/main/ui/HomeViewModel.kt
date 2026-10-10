package dev.jason.app.compose.vaultchat.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jason.app.compose.vaultchat.core.AppEvent
import dev.jason.app.compose.vaultchat.core.AppEvents
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.model.user.User
import dev.jason.app.compose.vaultchat.core.model.user.UserUi
import dev.jason.app.compose.vaultchat.core.model.user.toUi
import dev.jason.app.compose.vaultchat.core.model.user.toUser
import dev.jason.app.compose.vaultchat.main.domain.ConnectionsService
import dev.jason.app.compose.vaultchat.main.domain.UserApiRepository
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userApiRepository: UserApiRepository,
    private val connectionsService: ConnectionsService
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: HomeUiAction) {
        when (action) {
            is HomeUiAction.OnUiStateChange -> {
                _uiState.update {
                    action.state(it)
                }
            }

            HomeUiAction.Search -> search()
        }
    }

    private fun search() {
        val searchQuery = _uiState.value.searchQuery
        if (searchQuery.isBlank()) return

        viewModelScope.launch {
            val searchResults = userApiRepository.searchUser(searchQuery)
            _uiState.update {
                it.copy(
                    searchResults = searchResults.toUi(),
                    hasRequestedSearchAtLeastOnce = true
                )
            }
        }
    }

    private fun List<User>.toUi(): ImmutableList<UserUi> = map(User::toUi).toImmutableList()

    private fun updateConnections() {
        viewModelScope.launch {
            connectionsService.getConnections().collect { connections ->
                _uiState.update {
                    it.copy(
                        connections = connections.toUi(),
                        isLoading = false
                    )
                }

                AppState.apply {
                    if (otherUser.value?.toUser() in connections) {
                        connections.find { it.uid == otherUser.value?.uid }
                            ?.let { updateOtherUser(it.toUi()) }
                    }
                }
            }
        }
    }

    init {
        updateConnections()
        AppEvents.sendEvent(AppEvent.FetchAndAddMessagesToDatabase)
    }
}