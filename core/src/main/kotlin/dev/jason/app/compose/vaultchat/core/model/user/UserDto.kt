package dev.jason.app.compose.vaultchat.core.model.user

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val uid: String,
    val displayName: String,
    val profilePictureUrl: String,
    val status: Status
) {
    @Serializable
    enum class Status {
        Offline, Online
    }
}

fun UserDto.toUser() = User(
    uid = uid,
    displayName = displayName,
    profilePictureUrl = profilePictureUrl,
    status = status.toStatus()
)

fun User.toDto() = UserDto(
    uid = uid,
    displayName = displayName,
    profilePictureUrl = profilePictureUrl,
    status = status.toStatus()
)

private fun UserDto.Status.toStatus() = User.Status.valueOf(this.name)
private fun User.Status.toStatus() = UserDto.Status.valueOf(this.name)
