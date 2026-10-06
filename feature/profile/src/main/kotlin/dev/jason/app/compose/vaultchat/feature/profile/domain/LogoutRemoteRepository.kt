package dev.jason.app.compose.vaultchat.feature.profile.domain

interface LogoutRemoteRepository {

    suspend fun sendLogoutRequest(deviceId: String, clearMessages: Boolean)
    suspend fun sendDeleteCurrentDeviceRequest()
}