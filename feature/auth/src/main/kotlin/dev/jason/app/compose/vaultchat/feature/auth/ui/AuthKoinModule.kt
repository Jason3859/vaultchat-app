package dev.jason.app.compose.vaultchat.feature.auth.ui

import dev.jason.app.compose.vaultchat.feature.auth.data.AuthApiService
import dev.jason.app.compose.vaultchat.feature.auth.data.FirebaseGoogleAuthentication
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authModule = module {

    viewModelOf(::AuthViewModel)

    singleOf(::AuthApiService)
    singleOf(::FirebaseGoogleAuthentication)
}
