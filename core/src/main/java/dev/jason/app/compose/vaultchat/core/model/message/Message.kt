package dev.jason.app.compose.vaultchat.core.model.message

import dev.jason.app.compose.vaultchat.core.model.user.User
import java.time.LocalDateTime

data class Message(
    val id: String? = null, // only null while sending to backend, not while receiving from backend.
    val from: User,
    val to: User,
    val text: String,
    val timestamp: LocalDateTime
)