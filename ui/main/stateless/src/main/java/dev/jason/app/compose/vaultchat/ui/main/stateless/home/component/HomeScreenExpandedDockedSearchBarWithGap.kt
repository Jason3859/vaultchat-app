package dev.jason.app.compose.vaultchat.ui.main.stateless.home.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExpandedDockedSearchBarWithGap
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.jason.app.compose.vaultchat.core.model.user.UserUi
import dev.jason.app.compose.vaultchat.ui.main.stateless.R
import dev.jason.app.compose.vaultchat.ui.main.stateless.home.state.HomeUiState

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
internal fun HomeScreenExpandedDockedSearchBarWithGap(
    searchBarState: SearchBarState,
    inputField: @Composable () -> Unit,
    uiState: HomeUiState,
    onNonConnectedUserClick: (UserUi) -> Unit
) {
    ExpandedDockedSearchBarWithGap(
        state = searchBarState,
        inputField = inputField,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn {
            if (uiState.hasRequestedSearchAtLeastOnce) {
                if (uiState.searchResults.isNotEmpty()) {
                    itemsIndexed(uiState.searchResults) { index, user ->
                        UserItem(
                            selected = false,
                            count = uiState.searchResults.count(),
                            user = user,
                            onUserClick = onNonConnectedUserClick,
                            index = index,
                        )
                    }
                } else {
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(stringResource(R.string.no_users_found))
                        }
                    }
                }
            }
        }
    }
}