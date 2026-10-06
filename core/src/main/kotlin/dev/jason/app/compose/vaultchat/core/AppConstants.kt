package dev.jason.app.compose.vaultchat.core

import dev.jason.app.compose.vaultchat.core.model.device.Device
import dev.jason.app.compose.vaultchat.core.model.device.DeviceUi
import dev.jason.app.compose.vaultchat.core.model.user.User
import dev.jason.app.compose.vaultchat.core.model.user.UserUi
import kotlinx.collections.immutable.toImmutableList

object AppConstants {

    const val URL_HOST = "127.0.0.1:8080"
    const val BASE_HTTP_URL = "http://$URL_HOST"
    const val BASE_WS_URL = "ws://$URL_HOST"
//    const val BASE_URL = "https://vaultchat-67jk.onrender.com"

    const val TEXT_PLAIN = "text/plain"
    const val EXTRA_KEY_OTHER_USER_UID = "uid"
    const val ACTION_START_MAIN_ACTIVITY = "dev.jason.app.compose.vaultchat.main.ACTION_START_MAIN_ACTIVITY"

    const val ACTION = "ACTION"
    const val ACTION_NAVIGATE_TO_MESSAGING_SCREEN = "NAVIGATE_TO_MESSAGING_SCREEN"

    const val EVENT = "event"
    const val EVENT_NEW_MESSAGE = "new_message"
    const val EVENT_LAUNCH_FROM_AUTH_ACTIVITY = "launch_from_auth_activity"
    const val EVENT_LAUNCH_FROM_SHARE_ACTIVITY = "EVENT_LAUNCH_FROM_SHARE_ACTIVITY"

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
}