package dev.jason.app.compose.vaultchat.feature.messaging.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import dev.jason.app.compose.vaultchat.core.model.message.MessageUi
import dev.jason.app.compose.vaultchat.core.model.user.UserUi
import dev.jason.app.compose.vaultchat.ui.main.stateless.messaging.action.MessagingUiAction
import dev.jason.app.compose.vaultchat.ui.main.stateless.messaging.model.MessageStatus
import kotlinx.collections.immutable.ImmutableList


@Composable
internal fun MessagingScreenContent(
    messages: MessagePagingState,
    innerPadding: PaddingValues,
    otherUser: UserUi,
    onAction: (MessagingUiAction) -> Unit,
    pendingMessages: ImmutableList<MessageUi>,
    failedMessages: ImmutableList<MessageUi>
) {

    val pagingItems = messages.data.collectAsLazyPagingItems()
    val lazyListState = rememberLazyListState()

    val totalItemCount = pagingItems.itemCount + pendingMessages.size + failedMessages.size

    LaunchedEffect(totalItemCount) {
        if (totalItemCount > 0) {
            lazyListState.animateScrollToItem(totalItemCount - 1)
        }
    }

    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        item { Spacer(Modifier.height(6.dp)) }

        items(
            count = pagingItems.itemCount
        ) { index ->
            val msg = pagingItems[index] ?: return@items
            MessageItem(
                msg = msg,
                isOtherUser = otherUser.uid == msg.from,
                status = MessageStatus.Sent,
                onAction = onAction
            )
        }

        items(pendingMessages) { msg ->
            MessageItem(
                msg = msg,
                isOtherUser = otherUser.uid == msg.from,
                status = MessageStatus.Pending,
                onAction = onAction
            )
        }

        items(failedMessages) { msg ->
            MessageItem(
                msg = msg,
                isOtherUser = otherUser.uid == msg.from,
                status = MessageStatus.Failed,
                onAction = onAction
            )
        }
    }
}