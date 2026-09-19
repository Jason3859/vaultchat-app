package dev.jason.app.compose.vaultchat.ui.main.action

import dev.jason.app.compose.vaultchat.ui.main.state.MessagingUiState

interface MessagingUiAction {

    data class UpdateState(val state: (MessagingUiState) -> MessagingUiState) : MessagingUiAction

    data object SendMessage : MessagingUiAction

    data class OpenLink(val url: String) : MessagingUiAction
}