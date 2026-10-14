package dev.jason.app.compose.vaultchat.main.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.jason.app.compose.vaultchat.core.model.user.UserUi
import dev.jason.app.compose.vaultchat.core.ui.theme.VaultChatTheme
import dev.jason.app.compose.vaultchat.main.R
import dev.jason.app.compose.vaultchat.main.ui.HomeUiState

@Composable
internal fun HomeScreenContent(
    innerPadding: PaddingValues,
    uiState: HomeUiState,
    selectedUserUid: String?,
    onUserClick: (UserUi) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            contentPadding = innerPadding + PaddingValues(12.dp)
        ) {
            if (uiState.connections.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(R.string.no_recent_connections))
                    }
                }
            } else {
                itemsIndexed(uiState.connections) { index, user ->
                    UserItem(
                        selected = selectedUserUid == user.uid,
                        count = uiState.connections.count(),
                        user = user,
                        onUserClick = onUserClick,
                        index = index
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenContentLightModePreview() {
    VaultChatTheme {
        HomeScreenContent(
            innerPadding = PaddingValues(),
            uiState = HomeUiState.forPreview(),
            selectedUserUid = "TODO()",
            onUserClick = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
private fun HomeScreenContentDarkModePreview() {
    VaultChatTheme {
        HomeScreenContent(
            innerPadding = PaddingValues(),
            uiState = HomeUiState.forPreview(),
            selectedUserUid = "TODO()",
            onUserClick = {}
        )
    }
}