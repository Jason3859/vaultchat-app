package dev.jason.app.compose.vaultchat.feature.device

import android.util.Log
import dev.jason.app.compose.vaultchat.core.AppConstants.BASE_HTTP_URL
import dev.jason.app.compose.vaultchat.core.AppEvent
import dev.jason.app.compose.vaultchat.core.AppEvents
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.ToastController
import dev.jason.app.compose.vaultchat.core.model.device.Device
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeviceApiService(private val repository: DeviceApiRepository, private val client: HttpClient) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)

    suspend fun getDevices(): List<Device> {
        return try {
            repository.getDevices()
        } catch (e: Exception) {
            Log.e("DeviceApiService", "getDevices: exception occurred", e)
            ToastController.showErrorOccurredToast()
            emptyList()
        }
    }
}