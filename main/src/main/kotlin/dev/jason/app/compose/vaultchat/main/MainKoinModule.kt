package dev.jason.app.compose.vaultchat.main

import dev.jason.app.compose.vaultchat.main.data.ConnectionsApiRepoImpl
import dev.jason.app.compose.vaultchat.main.data.ConnectionsDao
import dev.jason.app.compose.vaultchat.main.data.ConnectionsDatabase
import dev.jason.app.compose.vaultchat.main.data.ConnectionsDbRepoImpl
import dev.jason.app.compose.vaultchat.main.data.UserApiRepoImpl
import dev.jason.app.compose.vaultchat.main.domain.ConnectionsApiRepository
import dev.jason.app.compose.vaultchat.main.domain.ConnectionsDbRepository
import dev.jason.app.compose.vaultchat.main.domain.ConnectionsService
import dev.jason.app.compose.vaultchat.main.domain.UserApiRepository
import dev.jason.app.compose.vaultchat.main.ui.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val mainModule = module {
    viewModelOf(::HomeViewModel)
    singleOf(::UserApiRepoImpl) { bind<UserApiRepository>() }
    singleOf(::ConnectionsService) { createdAtStart() }
    singleOf(::ConnectionsDbRepoImpl) { bind<ConnectionsDbRepository>() }
    singleOf(::ConnectionsApiRepoImpl) { bind<ConnectionsApiRepository>() }
    single<ConnectionsDao> { ConnectionsDatabase.getDatabase(androidContext()).dao() }
}