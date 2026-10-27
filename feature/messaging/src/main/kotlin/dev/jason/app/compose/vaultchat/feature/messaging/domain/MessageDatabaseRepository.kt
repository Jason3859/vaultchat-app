package dev.jason.app.compose.vaultchat.feature.messaging.domain

import androidx.paging.PagingData
import dev.jason.app.compose.vaultchat.core.model.message.Message
import dev.jason.app.compose.vaultchat.core.model.message.MessageUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface MessageDatabaseRepository {

    fun getMessagesPaginated(
        currentUserUid: String,
        otherUserUid: String,
        coroutineScope: CoroutineScope
    ): Flow<PagingData<MessageUi>>

    suspend fun getAllMessages(): List<MessageUi>

    suspend fun addMessage(message: Message)
    suspend fun addMessages(messages: List<Message>)
    suspend fun deleteAllMessages()
    suspend fun deleteMessageHistory(currentUserUid: String, otherUserUid: String)
}