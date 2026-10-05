package dev.jason.app.compose.vaultchat.ui.main.stateless.home.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.jason.app.compose.vaultchat.core.model.user.UserUi
import dev.jason.app.compose.vaultchat.ui.main.stateless.R
import dev.jason.app.compose.vaultchat.ui.main.stateless.home.state.HomeUiState

@Composable
internal fun HomeScreenContent(
    innerPadding: PaddingValues,
    uiState: HomeUiState,
    selectedUserUid: String?,
    onUserClick: (UserUi) -> Unit
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