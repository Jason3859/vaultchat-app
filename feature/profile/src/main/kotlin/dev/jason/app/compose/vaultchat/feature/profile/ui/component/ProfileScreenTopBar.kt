package dev.jason.app.compose.vaultchat.feature.profile.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@Composable
internal fun ProfileScreenTopBar(onBack: () -> Unit) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(
                onClick = onBack,
                colors = IconButtonDefaults.iconButtonVibrantColors()
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
        }
    )
}