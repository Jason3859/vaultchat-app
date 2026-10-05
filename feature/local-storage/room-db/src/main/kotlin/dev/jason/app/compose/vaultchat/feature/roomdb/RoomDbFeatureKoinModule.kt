package dev.jason.app.compose.vaultchat.feature.roomdb

import dev.jason.app.compose.vaultchat.feature.roomdb.messages.MessageDao
import dev.jason.app.compose.vaultchat.feature.roomdb.messages.MessageDatabase
import dev.jason.app.compose.vaultchat.feature.roomdb.messages.MessageDatabaseRepoImpl
import dev.jason.app.compose.vaultchat.feature.roomdb.messages.MessageDatabaseRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val roomDbFeatureModule = module {
    singleOf(::MessageDatabaseRepoImpl) {
        bind<MessageDatabaseRepository>()
        createdAtStart()
    }

    single<MessageDao> {
        MessageDatabase.getInstance(androidContext()).dao
    }
}
