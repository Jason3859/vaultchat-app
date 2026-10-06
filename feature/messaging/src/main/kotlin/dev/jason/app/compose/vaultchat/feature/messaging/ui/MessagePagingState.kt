package dev.jason.app.compose.vaultchat.feature.messaging.ui

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import dev.jason.app.compose.vaultchat.core.model.message.MessageUi
import kotlinx.coroutines.flow.Flow

@Immutable
data class MessagePagingState(
    val data: Flow<PagingData<MessageUi>>
)