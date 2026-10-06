package dev.jason.app.compose.vaultchat.ui.main.stateful

import dev.jason.app.compose.vaultchat.ui.main.stateful.home.HomeViewModel
import dev.jason.app.compose.vaultchat.ui.main.stateful.profile.ProfileViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val mainUiModule = module {
    viewModelOf(::HomeViewModel)
}
