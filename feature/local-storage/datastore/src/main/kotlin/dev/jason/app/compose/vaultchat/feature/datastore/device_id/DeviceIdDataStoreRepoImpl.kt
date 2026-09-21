package dev.jason.app.compose.vaultchat.feature.datastore.device_id

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.jason.app.compose.vaultchat.core.AppEvent
import dev.jason.app.compose.vaultchat.core.AppEvents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DeviceIdDataStoreRepoImpl(private val dataStore: DataStore<Preferences>) : DeviceIdDataStoreRepository {

    private companion object {

        private val coroutineScope = CoroutineScope(Dispatchers.Default)

        private const val DEVICE_ID_DATASTORE_NAME = "device_id_datastore_name"
        private val DEVICE_ID_PREFERENCE_KEY = stringPreferencesKey(DEVICE_ID_DATASTORE_NAME)
    }

    override suspend fun getCurrentDeviceId(): String? {
        return dataStore.data
            .map { prefs -> prefs[DEVICE_ID_PREFERENCE_KEY] }
            .first()
    }

    private suspend fun saveCurrentDeviceId(deviceId: String) {
        dataStore.updateData {
            it.toMutablePreferences().apply {
                this[DEVICE_ID_PREFERENCE_KEY] = deviceId
            }
        }
    }

    init {
        coroutineScope.launch {
            AppEvents.events.collect { event ->
                if (event is AppEvent.SaveCurrentDeviceId) {
                    saveCurrentDeviceId(event.deviceId)
                }
            }
        }
    }
}