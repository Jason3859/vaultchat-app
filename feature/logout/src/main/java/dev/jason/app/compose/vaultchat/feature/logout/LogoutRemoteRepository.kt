package dev.jason.app.compose.vaultchat.feature.logout

interface LogoutRemoteRepository {

    suspend fun sendLogoutRequest(
        deviceId: String,
        clearMessages: Boolean,
        onLogoutSuccessful: () -> Unit
    ): Int

    suspend fun sendDeleteCurrentDeviceRequest()
}