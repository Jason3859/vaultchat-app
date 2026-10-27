package dev.jason.app.compose.vaultchat.feature.messaging.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.jason.app.compose.vaultchat.core.model.message.Message
import dev.jason.app.compose.vaultchat.core.model.message.MessageUi
import dev.jason.app.compose.vaultchat.core.model.message.asString
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    val from: String,
    val to: String,
    val text: String,
    val timestamp: Long
)

fun MessageEntity.toUi() = MessageUi(
    from = from,
    to = to,
    text = text,
    timestamp = timestamp.toLocalDateTime().asString()
)

fun Message.toEntity() = MessageEntity(
    id = id!!,
    from = from.uid,
    to = to.uid,
    text = text,
    timestamp = timestamp.toLong()
)

fun LocalDateTime.toLong() = this
    .atZone(ZoneId.systemDefault())
    .toInstant()
    .toEpochMilli()

fun Long.toLocalDateTime(): LocalDateTime =
    Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
