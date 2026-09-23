package dev.jason.app.compose.vaultchat.feature.logout

import dev.jason.app.compose.vaultchat.core.AppConstants.BASE_HTTP_URL
import dev.jason.app.compose.vaultchat.core.AppState
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType

internal class LogoutRemoteRepoImpl(
    private val client: HttpClient
) : LogoutRemoteRepository {

    override suspend fun sendLogoutRequest(
        deviceId: String,
        clearMessages: Boolean,
        onLogoutSuccessful: () -> Unit
    ): Int {
        val response = client.delete("$BASE_HTTP_URL/request-logout") {
            contentType(ContentType.Application.Json)
            parameter("deviceId", deviceId)
            parameter("clear_messages", clearMessages.toString())
        }

        return response.status.value.also { onLogoutSuccessful.invoke() }
    }

    override suspend fun sendDeleteCurrentDeviceRequest() {
        client.delete("$BASE_HTTP_URL/logout") {
            val currentUser = AppState.currentUser.value!!
            val currentDevice = AppState.currentDevice.value!!

            parameter("uid", currentUser.uid)
            parameter("deviceId", currentDevice.id!!)
        }
    }
}