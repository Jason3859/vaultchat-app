package dev.jason.app.compose.vaultchat.feature.logout

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dev.jason.app.compose.vaultchat.core.AppEvent
import dev.jason.app.compose.vaultchat.core.AppEvents
import dev.jason.app.compose.vaultchat.core.ToastController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LogoutService(
    private val repository: LogoutRemoteRepository
) {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    suspend fun sendLogoutRequest(
        deviceId: String,
        clearMessages: Boolean
    ) {
        try {
            repository.sendLogoutRequest(deviceId, clearMessages)
        } catch (e: Exception) {
            Log.e("LogoutService", "beginLogout: exception occurred", e)
            ToastController.showErrorOccurredToast()
        }
    }

    suspend fun logoutCurrentDevice(clearMessages: Boolean) {
        repository.sendDeleteCurrentDeviceRequest()
        if (clearMessages) AppEvents.sendEvent(AppEvent.DeleteAllMessages)
        Firebase.auth.signOut()
        AppEvents.sendEvent(AppEvent.RestartApp)
    }

    init {
        coroutineScope.launch {
            AppEvents.events.collect { event ->
                if (event is AppEvent.LogoutCurrentDevice) {
                    val clearMessages = event.clearMessages
                    logoutCurrentDevice(clearMessages)
                }
            }
        }
    }
}