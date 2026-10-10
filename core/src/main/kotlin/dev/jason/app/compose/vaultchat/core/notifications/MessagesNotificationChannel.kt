package dev.jason.app.compose.vaultchat.core.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import dev.jason.app.compose.vaultchat.core.AppConstants
import dev.jason.app.compose.vaultchat.core.AppEvent
import dev.jason.app.compose.vaultchat.core.AppEvents
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.R
import dev.jason.app.compose.vaultchat.core.model.message.Message
import dev.jason.app.compose.vaultchat.core.notifications.NotificationChannel.MESSAGES
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MessagesNotificationChannel(private val context: Context) :
    AppNotificationChannel(
        context.getString(R.string.messages_channel_id),
        context.getString(R.string.messages_channel_name)
    ) {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    @Suppress("UNCHECKED_CAST")
    override fun showNotification(vararg args: Any?) {
        val message = args.first() as? Message ?: return
        if (message.from == AppState.currentUser.value) return

        val intent = Intent(AppConstants.ACTION_START_MAIN_ACTIVITY).apply {
            putExtra(AppConstants.EVENT, AppConstants.EVENT_NEW_MESSAGE)
            putExtra(AppConstants.EXTRA_KEY_OTHER_USER, message.from)

            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, flags)

        val notificationManager = AppNotificationManager(context)
        val notification = NotificationCompat.Builder(context, id)
            .setContentTitle(message.from.displayName)
            .setContentText(message.text)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // FIXME: to be replaced
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.showNotification(notification, this)
    }

    override fun channel(): NotificationChannel {
        return NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
    }

    init {
        coroutineScope.launch {
            AppEvents.events.collect { event ->
                if (event is AppEvent.ShowNotification) {
                    val (title, text, channel, extra) = event

                    if (channel == MESSAGES) {
                        showNotification(extra)
                    }
                }
            }
        }
    }
}