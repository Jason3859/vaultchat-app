package dev.jason.app.compose.vaultchat.ui.main.stateless.home.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.jason.app.compose.vaultchat.core.model.user.UserUi
import dev.jason.app.compose.vaultchat.core.ui.LoadProfilePicture

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
internal fun UserItem(
    selected: Boolean,
    count: Int,
    user: UserUi,
    onUserClick: (UserUi) -> Unit,
    index: Int
) {
    val profilePictureModifier = Modifier
        .size(40.dp)
        .clip(CircleShape)

    SegmentedListItem(
        modifier = Modifier.fillMaxWidth(),
        selected = selected,
        onClick = { onUserClick(user) },
        shapes = ListItemDefaults.segmentedShapes(
            index = index,
            count = count
        ),
        leadingContent = {
            LoadProfilePicture(
                url = user.profilePictureUrl,
                modifier = profilePictureModifier
            )
        }
    ) {
        Text(user.displayName)
    }
}