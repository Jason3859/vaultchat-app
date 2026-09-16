package dev.jason.app.compose.vaultchat.feature.datastore.device_id

interface DeviceIdDataStoreRepository {

    suspend fun getCurrentDeviceId(): String?
    suspend fun saveCurrentDeviceId(deviceId: String)
}