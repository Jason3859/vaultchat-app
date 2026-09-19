package dev.jason.app.compose.vaultchat.ui.main.stateless.profile

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

@Composable
internal fun ProfileScreenActionButtonsRow(
    isOwnProfile: Boolean,
    onActionClick: (ProfileScreenRowItemId) -> Unit
) {
    val items = if (isOwnProfile) {
        listOf(
            ProfileScreenRowItem(
                id = ProfileScreenRowItemId.Devices,
                name = ProfileScreenRowItemId.Devices.toString(),
                icon = Icons.Default.Devices
            ),
            ProfileScreenRowItem(
                id = ProfileScreenRowItemId.Blocklist,
                name = ProfileScreenRowItemId.Blocklist.toString(),
                icon = Icons.Default.Block
            ),
            ProfileScreenRowItem(
                id = ProfileScreenRowItemId.Logout,
                name = ProfileScreenRowItemId.Logout.toString(),
                icon = Icons.AutoMirrored.Filled.Logout
            )
        )
    } else {
        listOf(
            ProfileScreenRowItem(
                id = ProfileScreenRowItemId.BlockUser,
                name = "Block user",
                icon = Icons.Default.Block
            ),
            ProfileScreenRowItem(
                id = ProfileScreenRowItemId.DeleteAllMessages,
                name = "Delete all messages",
                icon = Icons.Default.DeleteForever
            )
        )
    }

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        items.forEach { (id, name, icon) ->
            ProfileScreenRowItem(
                name = name,
                icon = icon,
                onClick = { onActionClick(id) }
            )
        }
    }
}