package dev.jason.app.compose.vaultchat.main.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import dev.jason.app.compose.vaultchat.ui.main.stateless.R
import dev.jason.app.compose.vaultchat.main.ui.HomeUiAction
import dev.jason.app.compose.vaultchat.main.ui.HomeUiState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun HomeScreenAppBarInputField(
    uiState: HomeUiState,
    onAction: (HomeUiAction) -> Unit,
    searchBarState: SearchBarState
) {

    val coroutineScope = rememberCoroutineScope()

    SearchBarDefaults.InputField(
        query = uiState.searchQuery,
        onQueryChange = { string ->
            onAction(
                HomeUiAction.OnUiStateChange { it.copy(searchQuery = string) }
            )
        },
        onSearch = { onAction(HomeUiAction.Search) },
        expanded = searchBarState.currentValue != SearchBarValue.Collapsed,
        onExpandedChange = { expanded ->
            coroutineScope.launch {
                if (expanded) {
                    searchBarState.animateToExpanded()
                } else {
                    searchBarState.animateToCollapsed()
                    onAction(
                        HomeUiAction.OnUiStateChange {
                            it.copy(
                                searchQuery = "",
                                searchResults = persistentListOf()
                            )
                        }
                    )
                }
            }
        },
        placeholder = { Text(stringResource(R.string.search_for_users)) },
        leadingIcon = { Icon(Icons.Default.Search, null) }
    )
}