package dev.jason.app.compose.vaultchat.feature.datastore

import dev.jason.app.compose.vaultchat.feature.datastore.deviceid.DeviceIdDataStoreRepoImpl
import dev.jason.app.compose.vaultchat.feature.datastore.deviceid.DeviceIdDataStoreRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataStoreFeatureModule = module {

    singleOf(::DeviceIdDataStoreRepoImpl) {
        createdAtStart()
        bind<DeviceIdDataStoreRepository>()
    }
}
