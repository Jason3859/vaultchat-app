package dev.jason.app.compose.vaultchat.feature.auth

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val AuthFeatureKoinModule = module {

    singleOf(::AuthApiService)
    singleOf(::FirebaseGoogleAuthentication)
}