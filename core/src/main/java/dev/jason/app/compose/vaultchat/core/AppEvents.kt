package dev.jason.app.compose.vaultchat.core

import dev.jason.app.compose.vaultchat.core.model.message.Message
import dev.jason.app.compose.vaultchat.core.model.user.User
import dev.jason.app.compose.vaultchat.core.notifications.NotificationChannel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object AppEvents {

    private val _events = MutableSharedFlow<AppEvent>(1, 64, BufferOverflow.DROP_OLDEST)
    val events = _events.asSharedFlow()

    fun sendEvent(event: AppEvent) {
        _events.tryEmit(event)
    }
}

sealed interface AppEvent {

    data class UpdateConnectionStatus(val uid: String, val status: User.Status) : AppEvent

    data class AddMessage(val message: Message) : AppEvent
    data object DeleteAllMessages : AppEvent

    data object ReFetchConnections : AppEvent

    data class SaveCurrentDeviceId(val deviceId: String) : AppEvent

    data class LogoutCurrentDevice(val clearMessages: Boolean) : AppEvent

    data object RestartApp : AppEvent

    data class ShowNotification(
        val title: String,
        val content: String,
        val channel: NotificationChannel,
        val extras: Any? = null
    ) : AppEvent {
        companion object {
            fun newMessage(message: Message) = ShowNotification(
                "not required, get title and content from `message`", "also not required",
                NotificationChannel.MESSAGES, message
            )
        }
    }

    sealed interface NavEvent : AppEvent {

        data class NavigateToMessagingScreen(val uid: String) : NavEvent
        data object NavigateToHomeScreen : NavEvent
    }
}