package dev.jason.app.compose.vaultchat.feature.datastore.deviceid

interface DeviceIdDataStoreRepository {

    suspend fun getCurrentDeviceId(): String?
}
