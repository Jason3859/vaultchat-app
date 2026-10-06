package dev.jason.app.compose.vaultchat.feature.messaging.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.model.user.UserUi
import dev.jason.app.compose.vaultchat.core.ui.LoadingIndicator
import dev.jason.app.compose.vaultchat.feature.messaging.ui.component.AbstractMessagingScreen
import dev.jason.app.compose.vaultchat.feature.messaging.ui.component.MessagePagingState
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MessagingScreen(
    otherUser: UserUi,
    onBackClick: () -> Unit,
    onUserInfoClick: () -> Unit
) {
    val viewModel: MessagingViewModel = koinViewModel { parametersOf(otherUser) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val otherUser by viewModel.otherUser.collectAsStateWithLifecycle()
    val isOnline by AppState.isOnline.collectAsStateWithLifecycle()

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingIndicator()
        }
    } else if (otherUser != null) {
        AbstractMessagingScreen(
            otherUser = otherUser!!,
            onBackClick = onBackClick,
            uiState = uiState,
            onAction = viewModel::onAction,
            onUserInfoClick = onUserInfoClick,
            messages = MessagePagingState(viewModel.messages),
            pendingMessages = viewModel.pendingMessages.toImmutableList(),
            failedMessages = viewModel.failedMessages.toImmutableList(),
            isOffline = !isOnline
        )
    }
}
