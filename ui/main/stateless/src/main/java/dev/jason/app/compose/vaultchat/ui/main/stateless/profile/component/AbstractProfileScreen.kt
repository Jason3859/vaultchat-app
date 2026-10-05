package dev.jason.app.compose.vaultchat.ui.main.stateless.profile.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.jason.app.compose.vaultchat.core.ui.theme.VaultChatTheme
import dev.jason.app.compose.vaultchat.ui.main.stateless.defaultDevices
import dev.jason.app.compose.vaultchat.ui.main.stateless.defaultUsers
import dev.jason.app.compose.vaultchat.ui.main.stateless.profile.action.ProfileUiAction
import dev.jason.app.compose.vaultchat.ui.main.stateless.profile.state.ProfileUiState

@Composable
fun AbstractProfileScreen(
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
        AbstractProfileScreen(
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