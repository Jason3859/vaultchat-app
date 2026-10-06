package dev.jason.app.compose.vaultchat.main.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import dev.jason.app.compose.vaultchat.core.model.user.UserUi
import dev.jason.app.compose.vaultchat.core.ui.theme.VaultChatTheme
import dev.jason.app.compose.vaultchat.main.ui.HomeUiAction
import dev.jason.app.compose.vaultchat.main.ui.HomeUiState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AbstractHomeScreen(
    isOffline: Boolean,
    selectedUserUid: String?,
    uiState: HomeUiState,
    currentUserProfilePictureUrl: String?,
    onAction: (HomeUiAction) -> Unit,
    onUserClick: (UserUi) -> Unit,
    onNonConnectedUserClick: (UserUi) -> Unit,
    onProfileClick: () -> Unit,
) {
    val scrollBehavior = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            HomeScreenTopBar(
                scrollBehavior,
                uiState,
                onAction,
                isOffline,
                onProfileClick,
                currentUserProfilePictureUrl,
                onNonConnectedUserClick
            )
        }
    ) { innerPadding ->
        HomeScreenContent(innerPadding, uiState, selectedUserUid, onUserClick)
    }
}

private val previewContent = @Composable {
    VaultChatTheme {
        AbstractHomeScreen(
            isOffline = true,
            selectedUserUid = null,
            uiState = HomeUiState.forPreview(),
            currentUserProfilePictureUrl = "",
            onAction = {},
            onUserClick = { },
            onNonConnectedUserClick = {},
            onProfileClick = {}
        )
    }
}

@Preview
@Composable
private fun HomeScreenLightModePreview() {
    previewContent.invoke()
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
private fun HomeScreenDarkModePreview() {
    previewContent.invoke()
}