package dev.jason.app.compose.vaultchat.ui.main.stateless

import dev.jason.app.compose.vaultchat.core.model.device.Device
import dev.jason.app.compose.vaultchat.core.model.device.DeviceUi
import dev.jason.app.compose.vaultchat.core.model.user.User
import dev.jason.app.compose.vaultchat.core.model.user.UserUi
import kotlinx.collections.immutable.toImmutableList

val defaultDevices = List(10) { index ->
    DeviceUi(
        ownerUid = "owner",
        name = "device @$index",
        type = if (index % 2 == 0) Device.Type.Mobile else Device.Type.Tablet,
        os = Device.Os.Android,
        version = "10"
    )
}.toImmutableList()

val defaultUsers = List(20) { index ->
    UserUi(
        uid = "uid-$index",
        displayName = "User @$index",
        profilePictureUrl = "url @$index",
        status = User.Status.Online
    )
}.toImmutableList()