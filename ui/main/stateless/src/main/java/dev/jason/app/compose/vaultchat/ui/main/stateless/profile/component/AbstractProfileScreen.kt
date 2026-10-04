package dev.jason.app.compose.vaultchat.ui.main.stateless.profile.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.jason.app.compose.vaultchat.core.model.device.DeviceUi
import dev.jason.app.compose.vaultchat.core.model.user.UserUi
import dev.jason.app.compose.vaultchat.core.ui.theme.VaultChatTheme
import dev.jason.app.compose.vaultchat.ui.main.stateless.profile.action.ProfileUiAction
import dev.jason.app.compose.vaultchat.ui.main.stateless.home.state.HomeUiState
import dev.jason.app.compose.vaultchat.ui.main.stateless.profile.model.ProfileScreenRowItemId
import kotlinx.collections.immutable.ImmutableList

@Composable
fun AbstractProfileScreen(
    onBack: () -> Unit,
    user: UserUi,
    isOtherUserBlocked: Boolean? = null,
    devices: ImmutableList<DeviceUi>? = null,
    blocklist: ImmutableList<UserUi>? = null,
    onAction: (ProfileUiAction) -> Unit
) {
    var currentDialog by remember { mutableStateOf<ProfileScreenRowItemId?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ProfileScreenTopBar(onBack)
        }
    ) { innerPadding ->
        ProfileDialogs(
            onBack = onBack,
            currentDialog = currentDialog,
            onDismiss = { currentDialog = null },
            user = user,
            devices = devices,
            blocklist = blocklist,
            onAction = onAction
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ProfileScreenUserInfoSection(user)
            ProfileScreenActionButtonsRow(
                isOtherUserBlocked = isOtherUserBlocked,
                isOwnProfile = (devices != null && blocklist != null),
                onActionClick = { currentDialog = it }
            )
        }
    }
}

private val previewContent = @Composable {
    VaultChatTheme {
        AbstractProfileScreen(
            onBack = {},
            user = HomeUiState.defaultUsers.first(),
            devices = HomeUiState.defaultDevices,
            blocklist = HomeUiState.defaultUsers.subList(15, 20),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun ProfileScreenLightModePreview() {
    previewContent.invoke()
}