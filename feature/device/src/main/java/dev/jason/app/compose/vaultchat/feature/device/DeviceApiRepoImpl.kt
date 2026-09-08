package dev.jason.app.compose.vaultchat.feature.device

import dev.jason.app.compose.vaultchat.core.AppConstants.BASE_HTTP_URL
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.model.device.Device
import dev.jason.app.compose.vaultchat.core.model.device.DeviceDto
import dev.jason.app.compose.vaultchat.core.model.device.toDevice
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class DeviceApiRepoImpl(
    private val client: HttpClient
) : DeviceApiRepository {

    override suspend fun getDevices(): List<Device> {
        val response = client.get("$BASE_HTTP_URL/device/mine") {
            parameter("uid", AppState.currentUser.value?.uid ?: throw IllegalStateException("user is null"))
        }

        return response.body<List<DeviceDto>>()
            .map(DeviceDto::toDevice)
    }
}