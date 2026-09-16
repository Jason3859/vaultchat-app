package dev.jason.app.compose.vaultchat.feature.room_db.messages

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.jason.app.compose.vaultchat.core.model.message.Message
import dev.jason.app.compose.vaultchat.core.model.user.User
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

fun MessageEntity.toMessage() = Message(
    id = id,
    from = User.emptyUserWithUid(from),
    to = User.emptyUserWithUid(to),
    text = text,
    timestamp = timestamp.toLocalDateTime()
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