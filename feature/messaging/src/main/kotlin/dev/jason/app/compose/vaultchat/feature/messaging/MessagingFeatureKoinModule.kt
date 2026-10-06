package dev.jason.app.compose.vaultchat.feature.messaging

import dev.jason.app.compose.vaultchat.feature.messaging.data.local.MessageDao
import dev.jason.app.compose.vaultchat.feature.messaging.data.local.MessageDatabase
import dev.jason.app.compose.vaultchat.feature.messaging.data.local.MessageDatabaseRepoImpl
import dev.jason.app.compose.vaultchat.feature.messaging.data.remote.MessagingApiRepoImpl
import dev.jason.app.compose.vaultchat.feature.messaging.domain.MessageDatabaseRepository
import dev.jason.app.compose.vaultchat.feature.messaging.domain.MessagingApiRepository
import dev.jason.app.compose.vaultchat.feature.messaging.ui.MessagingViewModel
import io.ktor.client.HttpClient
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.websocket.ktor.KtorWebSocketClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val messagingFeatureModule = module {

    singleOf(::MessagingApiRepoImpl) { bind<MessagingApiRepository>() }

    single {
        StompClient(KtorWebSocketClient(get<HttpClient>()))
    }

    singleOf(::MessageDatabaseRepoImpl) {
        bind<MessageDatabaseRepository>()
        createdAtStart()
    }

    single<MessageDao> {
        MessageDatabase.getInstance(androidContext()).dao
    }

    viewModelOf(::MessagingViewModel)
}
