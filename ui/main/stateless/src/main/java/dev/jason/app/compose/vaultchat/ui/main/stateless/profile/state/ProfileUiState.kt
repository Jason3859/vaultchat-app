package dev.jason.app.compose.vaultchat.ui.main.stateless.profile.state

import dev.jason.app.compose.vaultchat.core.model.device.DeviceUi
import dev.jason.app.compose.vaultchat.core.model.user.UserUi
import dev.jason.app.compose.vaultchat.ui.main.stateless.profile.model.ProfileScreenDialogId
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class ProfileUiState(
    val user: UserUi? = null,
    val isOwnProfile: Boolean = false,
    val isOtherUserBlocked: Boolean = false,
    val currentDialog: ProfileScreenDialogId? = null,
    val devices: ImmutableList<DeviceUi> = persistentListOf(),
    val blocklist: ImmutableList<UserUi> = persistentListOf(),
)
