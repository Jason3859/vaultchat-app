package dev.jason.app.compose.vaultchat.feature.profile.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.jason.app.compose.vaultchat.core.AppConstants.defaultDevices
import dev.jason.app.compose.vaultchat.core.AppConstants.defaultUsers
import dev.jason.app.compose.vaultchat.core.ui.LoadingIndicator
import dev.jason.app.compose.vaultchat.core.ui.theme.VaultChatTheme
import dev.jason.app.compose.vaultchat.feature.profile.ui.component.ProfileDialogs
import dev.jason.app.compose.vaultchat.feature.profile.ui.component.ProfileScreenActionButtonsRow
import dev.jason.app.compose.vaultchat.feature.profile.ui.component.ProfileScreenTopBar
import dev.jason.app.compose.vaultchat.feature.profile.ui.component.ProfileScreenUserInfoSection
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    onBack: () -> Unit
) {
    val viewModel: ProfileViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.user != null) {
        ProfileScreen(
            uiState = uiState,
            onBack = onBack,
            onAction = viewModel::onAction
        )
    } else {
        LoadingScreen()
    }
}

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    uiState: ProfileUiState,
    onAction: (ProfileUiAction) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ProfileScreenTopBar(onBack)
        }
    ) { innerPadding ->
        ProfileDialogs(
            onBack = onBack,
            uiState = uiState,
            onAction = onAction
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ProfileScreenUserInfoSection(uiState.user!!)
            ProfileScreenActionButtonsRow(
                isOtherUserBlocked = uiState.isOtherUserBlocked,
                isOwnProfile = uiState.isOwnProfile,
                onActionClick = { onAction(ProfileUiAction.ShowDialog(it)) }
            )
        }
    }
}

private val previewContent = @Composable {
    VaultChatTheme {
        ProfileScreen(
            onBack = {},
            uiState = ProfileUiState(
                user = defaultUsers.first(),
                devices = defaultDevices,
                blocklist = defaultUsers.subList(15, 20)
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun ProfileScreenLightModePreview() {
    previewContent.invoke()
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