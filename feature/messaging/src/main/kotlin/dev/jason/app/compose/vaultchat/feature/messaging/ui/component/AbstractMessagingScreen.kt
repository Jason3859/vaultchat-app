package dev.jason.app.compose.vaultchat.feature.messaging.ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import dev.jason.app.compose.vaultchat.core.model.message.MessageUi
import dev.jason.app.compose.vaultchat.core.model.user.User
import dev.jason.app.compose.vaultchat.core.model.user.UserUi
import dev.jason.app.compose.vaultchat.core.ui.theme.VaultChatTheme
import dev.jason.app.compose.vaultchat.ui.main.stateless.messaging.action.MessagingUiAction
import dev.jason.app.compose.vaultchat.ui.main.stateless.messaging.state.MessagingUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Immutable
data class MessagePagingState(
    val data: Flow<PagingData<MessageUi>>
)

@Composable
fun AbstractMessagingScreen(
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
private fun AbstractMessagingScreenPreview() {
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
        AbstractMessagingScreen(
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