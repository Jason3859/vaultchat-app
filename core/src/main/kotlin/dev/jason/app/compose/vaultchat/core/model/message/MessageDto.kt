package dev.jason.app.compose.vaultchat.core.model.message

import dev.jason.app.compose.vaultchat.core.model.user.UserDto
import dev.jason.app.compose.vaultchat.core.model.user.toDto
import dev.jason.app.compose.vaultchat.core.model.user.toUser
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class MessageDto(
    val id: String?,
    val from: UserDto,
    val to: UserDto,
    val text: String,
    val timestamp: String
)

fun Message.toDto(): MessageDto = MessageDto(id, from.toDto(), to.toDto(), text, timestamp.toString())
fun MessageDto.toMessage() = Message(id, from.toUser(), to.toUser(), text, LocalDateTime.parse(timestamp))
