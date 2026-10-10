package dev.jason.app.compose.vaultchat.main.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.SearchBarScrollBehavior
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.rememberSearchBarWithGapState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.jason.app.compose.vaultchat.core.model.user.UserUi
import dev.jason.app.compose.vaultchat.main.ui.HomeUiAction
import dev.jason.app.compose.vaultchat.main.ui.HomeUiState


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