package dev.jason.app.compose.vaultchat.feature.messaging.data.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dev.jason.app.compose.vaultchat.core.AppEvent
import dev.jason.app.compose.vaultchat.core.AppEvents
import dev.jason.app.compose.vaultchat.core.model.message.Message
import dev.jason.app.compose.vaultchat.feature.messaging.domain.MessageDatabaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MessageDatabaseRepoImpl(private val dao: MessageDao) : MessageDatabaseRepository {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    override fun getMessagesPaginated(
        currentUserUid: String,
        otherUserUid: String,
        coroutineScope: CoroutineScope
    ): Flow<PagingData<Message>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { dao.getMessages(currentUserUid, otherUserUid) }
        ).flow
            .cachedIn(coroutineScope)
            .map { pagingData ->
                pagingData.map { entity ->
                    entity.toMessage()
                }
            }
    }

    override suspend fun deleteMessageHistory(
        currentUserUid: String,
        otherUserUid: String
    ) {
        dao.deleteMessageHistory(currentUserUid, otherUserUid)
    }

    override suspend fun addMessage(message: Message) {
        dao.addMessage(message.toEntity())
    }

    override suspend fun addMessages(messages: List<Message>) {
        dao.addMessages(messages.map(Message::toEntity))
    }

    override suspend fun deleteAllMessages() {
        dao.deleteAllMessages()
    }

    override suspend fun getAllMessages(): List<Message> {
        return dao.getAllMessages().map(MessageEntity::toMessage)
    }

    init {
        coroutineScope.launch {
            AppEvents.events.collect { event ->
                when (event) {
                    is AppEvent.AddMessage -> addMessage(event.message)
                    is AppEvent.DeleteAllMessages -> deleteAllMessages()
                    else -> {}
                }
            }
        }
    }
}
