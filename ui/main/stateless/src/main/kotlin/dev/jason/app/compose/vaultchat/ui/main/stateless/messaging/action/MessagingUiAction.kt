package dev.jason.app.compose.vaultchat.ui.main.stateless.messaging.action

import dev.jason.app.compose.vaultchat.ui.main.stateless.messaging.state.MessagingUiState

interface MessagingUiAction {

    data class UpdateState(val state: (MessagingUiState) -> MessagingUiState) : MessagingUiAction

    data object SendMessage : MessagingUiAction

    data class OpenLink(val url: String) : MessagingUiAction
}