package dev.jason.app.compose.vaultchat.feature.messaging.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.model.message.MessageUi
import dev.jason.app.compose.vaultchat.core.model.user.User
import dev.jason.app.compose.vaultchat.core.model.user.UserUi
import dev.jason.app.compose.vaultchat.core.ui.LoadingIndicator
import dev.jason.app.compose.vaultchat.core.ui.theme.VaultChatTheme
import dev.jason.app.compose.vaultchat.feature.messaging.ui.component.MessagingScreenBottomBar
import dev.jason.app.compose.vaultchat.feature.messaging.ui.component.MessagingScreenContent
import dev.jason.app.compose.vaultchat.feature.messaging.ui.component.MessagingScreenTopBar
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MessagingScreen(
    onBackClick: () -> Unit,
    onUserInfoClick: () -> Unit
) {
    val viewModel: MessagingViewModel = koinViewModel()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val otherUser by AppState.otherUser.collectAsStateWithLifecycle()
    val isOnline by AppState.isOnline.collectAsStateWithLifecycle()

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingIndicator()
        }
    } else if (otherUser != null) {
        MessagingScreen(
            otherUser = otherUser!!,
            onBackClick = onBackClick,
            uiState = uiState,
            onAction = viewModel::onAction,
            onUserInfoClick = onUserInfoClick,
            messages = MessagePagingState(viewModel.messages),
            pendingMessages = viewModel.pendingMessages.toImmutableList(),
            failedMessages = viewModel.failedMessages.toImmutableList(),
            isOffline = !isOnline
        )
    }
}

@Composable
fun MessagingScreen(
    otherUser: UserUi,
    onBackClick: () -> Unit,
    uiState: MessagingUiState,
    onAction: (MessagingUiAction) -> Unit,
    onUserInfoClick: () -> Unit,
    messages: MessagePagingState,
    pendingMessages: ImmutableList<MessageUi>,
    failedMessages: ImmutableList<MessageUi>,
    isOffline: Boolean
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { MessagingScreenTopBar(otherUser, onBackClick, onUserInfoClick, isOffline) },
        bottomBar = { MessagingScreenBottomBar(uiState, onAction) }
    ) { innerPadding ->
        MessagingScreenContent(
            messages,
            innerPadding,
            otherUser,
            onAction,
            pendingMessages,
            failedMessages
        )
    }
}

@Preview
@Composable
private fun MessagingScreenPreview() {
    val me = "me"
    val other = "Other User"

    val list = List(20) { index ->
        MessageUi(
            from = if (index % 2 == 0) me else other,
            to = if (index % 2 == 0) other else me,
            text = "text-$index",
            timestamp = "2026-04-01T11:02:10.692796400"
        )
    }

    val messagesFlow = flowOf(PagingData.from(list))

    VaultChatTheme {
        MessagingScreen(
            otherUser = UserUi(other, other, other, User.Status.Online),
            onBackClick = {},
            uiState = MessagingUiState(),
            onAction = {},
            messages = MessagePagingState(messagesFlow),
            pendingMessages = list.subList(0, 5).toImmutableList(),
            failedMessages = list.subList(10, 15).toImmutableList(),
            isOffline = false,
            onUserInfoClick = {}
        )
    }
}