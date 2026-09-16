package dev.jason.app.compose.vaultchat.feature.room_db

import dev.jason.app.compose.vaultchat.feature.room_db.messages.MessageDao
import dev.jason.app.compose.vaultchat.feature.room_db.messages.MessageDatabase
import dev.jason.app.compose.vaultchat.feature.room_db.messages.MessageDatabaseRepoImpl
import dev.jason.app.compose.vaultchat.feature.room_db.messages.MessageDatabaseRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val RoomDbFeatureKoinModule = module {
    singleOf(::MessageDatabaseRepoImpl) {
        bind<MessageDatabaseRepository>()
        createdAtStart()
    }

    single<MessageDao> {
        MessageDatabase.getInstance(androidContext()).dao
    }
}