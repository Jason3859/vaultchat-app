package dev.jason.app.compose.vaultchat.core.model.user

import dev.jason.app.compose.vaultchat.core.model.device.Device
import dev.jason.app.compose.vaultchat.core.model.device.DeviceDto
import dev.jason.app.compose.vaultchat.core.model.device.toDto
import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserDto(
    val uid: String,
    val displayName: String,
    val profilePictureUrl: String,
    val device: DeviceDto
) {

    companion object {
        fun fromUserAndDevice(user: User, device: Device) =
            RegisterUserDto(
                uid = user.uid,
                displayName = user.displayName!!,
                profilePictureUrl = user.profilePictureUrl!!,
                device = device.toDto()
            )
    }
}