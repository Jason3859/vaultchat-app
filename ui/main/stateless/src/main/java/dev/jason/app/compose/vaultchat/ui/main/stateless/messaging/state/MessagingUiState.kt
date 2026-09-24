package dev.jason.app.compose.vaultchat.ui.main.stateless.messaging.state

data class MessagingUiState(
    val messageText: String = "",
    val sendButtonEnabled: Boolean = false,
    val isLoading: Boolean = false,
)