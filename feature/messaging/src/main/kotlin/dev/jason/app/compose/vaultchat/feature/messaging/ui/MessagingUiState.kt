package dev.jason.app.compose.vaultchat.feature.messaging.ui

data class MessagingUiState(
    val messageText: String = "",
    val sendButtonEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val isUserBlocked: Boolean = false,
)