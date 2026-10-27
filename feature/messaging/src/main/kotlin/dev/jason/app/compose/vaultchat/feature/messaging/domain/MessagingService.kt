package dev.jason.app.compose.vaultchat.feature.messaging.domain

import dev.jason.app.compose.vaultchat.core.AppEvent
import dev.jason.app.compose.vaultchat.core.AppEvents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MessagingService(
    private val apiRepository: MessagingApiRepository,
    private val databaseRepository: MessageDatabaseRepository
) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private suspend fun fetchAndAddMessagesToDatabase() {

        val fetchedMessages = apiRepository.fetchMessages()
        databaseRepository.addMessages(fetchedMessages)
    }

    init {
        coroutineScope.launch {
            AppEvents.events.collect { event ->
                if (event is AppEvent.FetchAndAddMessagesToDatabase) {
                    fetchAndAddMessagesToDatabase()
                }
            }
        }
    }
}