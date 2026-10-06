package dev.jason.app.compose.vaultchat.main.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.AppBarWithSearch
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBarScrollBehavior
import androidx.compose.material3.SearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.jason.app.compose.vaultchat.core.ui.LoadProfilePicture

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