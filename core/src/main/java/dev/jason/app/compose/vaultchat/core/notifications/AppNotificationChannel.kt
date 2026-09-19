package dev.jason.app.compose.vaultchat.core.notifications

import android.app.NotificationChannel

abstract class AppNotificationChannel(val id: String, val name: String) {

    abstract fun channel(): NotificationChannel
    abstract fun showNotification(vararg args: Any?)
}