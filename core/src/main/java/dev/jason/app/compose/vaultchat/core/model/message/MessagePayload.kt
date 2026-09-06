package dev.jason.app.compose.vaultchat.core.model.message

import kotlinx.serialization.Serializable

@Serializable
data class MessagePayload(
    val from: String,
    val to: String,
    val text: String,
    val timestamp: String,
)

fun Message.toPayload() = MessagePayload(from.uid, to.uid, text, timestamp.toString())