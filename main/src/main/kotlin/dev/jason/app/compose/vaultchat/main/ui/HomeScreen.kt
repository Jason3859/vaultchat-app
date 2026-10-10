package dev.jason.app.compose.vaultchat.main.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.model.user.UserUi
import dev.jason.app.compose.vaultchat.core.ui.theme.VaultChatTheme
import dev.jason.app.compose.vaultchat.main.ui.component.HomeScreenContent
import dev.jason.app.compose.vaultchat.main.ui.component.HomeScreenTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    onUserClick: (UserUi) -> Unit,
    onNewUserClick: (UserUi) -> Unit,
    onProfileClick: () -> Unit
) {
    val viewModel: HomeViewModel = koinViewModel()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isOnline by AppState.isOnline.collectAsStateWithLifecycle()
    val selectedUser by AppState.otherUser.collectAsStateWithLifecycle()
    val currentUser by AppState.currentUser.collectAsStateWithLifecycle()
    val currentUserProfilePictureUrl = currentUser?.profilePictureUrl!!

    HomeScreen(
        isOffline = !isOnline,
        selectedUserUid = selectedUser?.uid,
        uiState = uiState,
        currentUserProfilePictureUrl = currentUserProfilePictureUrl,
        onAction = viewModel::onAction,
        onUserClick = onUserClick,
        onNewUserClick = onNewUserClick,
        onProfileClick = onProfileClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeScreen(
    isOffline: Boolean,
    selectedUserUid: String?,
    uiState: HomeUiState,
    currentUserProfilePictureUrl: String?,
    onAction: (HomeUiAction) -> Unit,
    onUserClick: (UserUi) -> Unit,
    onNewUserClick: (UserUi) -> Unit,
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
                onNewUserClick
            )
        }
    ) { innerPadding ->
        HomeScreenContent(innerPadding, uiState, selectedUserUid, onUserClick)
    }
}

private val previewContent = @Composable {
    VaultChatTheme {
        HomeScreen(
            isOffline = true,
            selectedUserUid = null,
            uiState = HomeUiState.forPreview(),
            currentUserProfilePictureUrl = "",
            onAction = {},
            onUserClick = { },
            onNewUserClick = {},
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