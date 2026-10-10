package dev.jason.app.compose.vaultchat.feature.messaging.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.jason.app.compose.vaultchat.core.model.user.UserUi
import dev.jason.app.compose.vaultchat.core.ui.LoadProfilePicture
import dev.jason.app.compose.vaultchat.feature.messaging.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MessagingScreenTopBar(
    otherUser: UserUi,
    onBackClick: () -> Unit,
    onUserInfoClick: () -> Unit,
    isOffline: Boolean
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onUserInfoClick() }
            ) {
                LoadProfilePicture(
                    url = otherUser.profilePictureUrl,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(40.dp)
                )

                Spacer(Modifier.width(10.dp))

                Column {
                    Text(otherUser.displayName)
                    Text(
                        text = if (isOffline) stringResource(R.string.you_are_offline) else stringResource(
                            R.string.user_status_display_text,
                            otherUser.status.toString().lowercase()
                        ),
                        fontSize = 12.sp
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onBackClick) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, null)
            }
        },
        actions = {
            if (isOffline) {
                Icon(
                    imageVector = Icons.Default.WifiOff,
                    contentDescription = null,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    )
}