package dev.jason.app.compose.vaultchat.feature.messaging.domain

import dev.jason.app.compose.vaultchat.core.model.message.Message

interface MessagingApiRepository {

    suspend fun sendMessage(message: Message): Result<Unit>
    suspend fun fetchMessages(): List<Message>
}