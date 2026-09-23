package dev.jason.app.compose.vaultchat.feature.logout

interface LogoutRemoteRepository {

    suspend fun sendLogoutRequest(deviceId: String, clearMessages: Boolean)
    suspend fun sendDeleteCurrentDeviceRequest()
}