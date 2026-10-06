package dev.jason.app.compose.vaultchat.feature.profile.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Devices
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.jason.app.compose.vaultchat.ui.main.stateless.R
import dev.jason.app.compose.vaultchat.ui.main.stateless.profile.model.ProfileScreenDialogId

@Composable
internal fun ProfileScreenActionButtonsRow(
    isOwnProfile: Boolean,
    onActionClick: (ProfileScreenDialogId) -> Unit,
    isOtherUserBlocked: Boolean? = null
) {
    val items = if (isOwnProfile) {
        listOf(
            ProfileScreenRowItem(
                id = ProfileScreenDialogId.Devices,
                name = stringResource(R.string.your_devices),
                icon = Icons.Default.Devices
            ),
            ProfileScreenRowItem(
                id = ProfileScreenDialogId.Blocklist,
                name = stringResource(R.string.blocklist),
                icon = Icons.Default.Block
            ),
            ProfileScreenRowItem(
                id = ProfileScreenDialogId.Logout,
                name = stringResource(R.string.logout),
                icon = Icons.AutoMirrored.Filled.Logout
            )
        )
    } else {
        listOf(
            if (isOtherUserBlocked == true) {
                ProfileScreenRowItem(
                    id = ProfileScreenDialogId.UnblockUser,
                    name = stringResource(R.string.unblock),
                    icon = Icons.Default.Block
                )
            } else {
                ProfileScreenRowItem(
                    id = ProfileScreenDialogId.BlockUser,
                    name = stringResource(R.string.block),
                    icon = Icons.Default.Block
                )
            },
            ProfileScreenRowItem(
                id = ProfileScreenDialogId.ClearChat,
                name = stringResource(R.string.clear_chat),
                icon = Icons.Default.DeleteForever
            )
        )
    }

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        items.forEach { item ->
            ProfileScreenRowItem(
                item = item,
                onClick = { onActionClick(item.id) }
            )
        }
    }
}