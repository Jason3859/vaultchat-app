package dev.jason.app.compose.vaultchat.ui.main.action

import dev.jason.app.compose.vaultchat.ui.main.state.HomeUiState

sealed interface HomeUiAction {

    data object Search : HomeUiAction
    data class OnUiStateChange(val state: (HomeUiState) -> HomeUiState) : HomeUiAction
}