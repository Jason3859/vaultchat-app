package dev.jason.app.compose.vaultchat.ui.main.stateful.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.jason.app.compose.vaultchat.core.ui.LoadingIndicator
import dev.jason.app.compose.vaultchat.ui.main.stateless.profile.component.AbstractProfileScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    onBack: () -> Unit
) {
    val viewModel: ProfileViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.user != null) {
        AbstractProfileScreen(
            uiState = uiState,
            onBack = onBack,
            onAction = viewModel::onAction
        )
    } else {
        LoadingScreen()
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LoadingIndicator()
    }
}