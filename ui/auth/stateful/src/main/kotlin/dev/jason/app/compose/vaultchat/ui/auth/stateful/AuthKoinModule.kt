package dev.jason.app.compose.vaultchat.ui.auth.stateful

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authUiModule = module {
    viewModelOf(::AuthViewModel)
}
