package dev.jason.app.compose.vaultchat.ui.main.stateless.home.action

import dev.jason.app.compose.vaultchat.ui.main.stateless.home.state.HomeUiState

sealed interface HomeUiAction {

    data object Search : HomeUiAction
    data class OnUiStateChange(val state: (HomeUiState) -> HomeUiState) : HomeUiAction
}