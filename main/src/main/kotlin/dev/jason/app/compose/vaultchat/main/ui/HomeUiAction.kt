package dev.jason.app.compose.vaultchat.main.ui

sealed interface HomeUiAction {

    data object Search : HomeUiAction
    data class OnUiStateChange(val state: (HomeUiState) -> HomeUiState) : HomeUiAction
}