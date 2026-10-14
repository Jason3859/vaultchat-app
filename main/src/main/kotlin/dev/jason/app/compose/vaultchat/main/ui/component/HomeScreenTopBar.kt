package dev.jason.app.compose.vaultchat.main.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.AppBarWithSearch
import androidx.compose.material3.ExpandedDockedSearchBarWithGap
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarScrollBehavior
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSearchBarWithGapState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.jason.app.compose.vaultchat.core.model.user.UserUi
import dev.jason.app.compose.vaultchat.core.ui.LoadProfilePicture
import dev.jason.app.compose.vaultchat.main.R
import dev.jason.app.compose.vaultchat.main.ui.HomeUiAction
import dev.jason.app.compose.vaultchat.main.ui.HomeUiState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
internal fun HomeScreenTopBar(
    scrollBehavior: SearchBarScrollBehavior,
    uiState: HomeUiState,
    onAction: (HomeUiAction) -> Unit,
    isOffline: Boolean,
    onProfileClick: () -> Unit,
    currentUserProfilePictureUrl: String?,
    onUserClick: (UserUi) -> Unit
) {

    val searchBarState = rememberSearchBarWithGapState(initialValue = SearchBarValue.Collapsed)
    val inputField = @Composable { HomeScreenAppBarInputField(uiState, onAction, searchBarState) }

    Box {
        Column {
            HomeScreenAppBarWithSearch(
                scrollBehavior,
                searchBarState,
                inputField,
                isOffline,
                onProfileClick,
                currentUserProfilePictureUrl
            )

            if (uiState.isLoading) {
                LinearWavyProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        HomeScreenExpandedDockedSearchBarWithGap(
            searchBarState,
            inputField,
            uiState,
            onUserClick
        )
    }
}

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

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun HomeScreenAppBarWithSearch(
    scrollBehavior: SearchBarScrollBehavior,
    searchBarState: SearchBarState,
    inputField: @Composable () -> Unit,
    isOffline: Boolean,
    onProfileClick: () -> Unit,
    currentUserProfilePictureUrl: String?
) {
    AppBarWithSearch(
        scrollBehavior = scrollBehavior,
        state = searchBarState,
        inputField = inputField,
        colors = SearchBarDefaults.appBarWithSearchColors(
            appBarContainerColor = Color.Unspecified,
            searchBarColors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.background
            )
        ),
        actions = {
            val imageModifier = Modifier.size(50.dp)

            if (isOffline) {
                Icon(Icons.Default.WifiOff, null)
            }

            IconButton(onProfileClick) {
                LoadProfilePicture(
                    url = currentUserProfilePictureUrl!!,
                    modifier = imageModifier
                )
            }
        }
    )
}

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