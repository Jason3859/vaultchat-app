package dev.jason.app.compose.vaultchat.feature.messaging.ui

interface MessagingUiAction {

    data class UpdateState(val state: (MessagingUiState) -> MessagingUiState) : MessagingUiAction

    data object SendMessage : MessagingUiAction

    data class OpenLink(val url: String) : MessagingUiAction
}