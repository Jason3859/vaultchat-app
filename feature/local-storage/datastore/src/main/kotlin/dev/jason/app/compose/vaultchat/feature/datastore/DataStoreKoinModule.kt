package dev.jason.app.compose.vaultchat.feature.datastore

import dev.jason.app.compose.vaultchat.feature.datastore.device_id.DeviceIdDataStoreRepoImpl
import dev.jason.app.compose.vaultchat.feature.datastore.device_id.DeviceIdDataStoreRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val DataStoreKoinModule = module {

    singleOf(::DeviceIdDataStoreRepoImpl) {
        createdAtStart()
        bind<DeviceIdDataStoreRepository>()
    }
}