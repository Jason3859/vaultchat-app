package dev.jason.app.compose.vaultchat.feature.profile.data

import dev.jason.app.compose.vaultchat.core.AppConstants.BASE_HTTP_URL
import dev.jason.app.compose.vaultchat.core.AppEvent
import dev.jason.app.compose.vaultchat.core.AppEvents
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.model.device.Device
import dev.jason.app.compose.vaultchat.core.model.device.DeviceDto
import dev.jason.app.compose.vaultchat.core.model.device.toDevice
import dev.jason.app.compose.vaultchat.feature.profile.domain.DeviceApiRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeviceApiRepoImpl(
    private val client: HttpClient
) : DeviceApiRepository {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override suspend fun getDevices(): List<Device> {
        val response = client.get("${BASE_HTTP_URL}/device/mine") {
            parameter("uid", AppState.currentUser.value?.uid ?: throw IllegalStateException("user is null"))
        }

        return response.body<List<DeviceDto>>()
            .map(DeviceDto::toDevice)
    }

    init {
        coroutineScope.launch {
            val currentUser = AppState.currentUser.value!!
            val currentDevice = AppState.currentDevice.value!!

            client.get("$BASE_HTTP_URL/device/marked-for-logout?uid=${currentUser.uid}")
                .let { response ->
                    response.body<List<Map<String, String>>>()
                        .firstOrNull { it["device_id"] == currentDevice.id }
                        ?.let { map ->
                            val clearMessages = map["clear_messages"]?.toBoolean()!!
                            AppEvents.sendEvent(AppEvent.LogoutCurrentDevice(clearMessages))
                        }
                }
        }
    }
}