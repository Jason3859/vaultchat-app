package dev.jason.app.compose.vaultchat.ui.main.stateless.profile.model

import androidx.compose.ui.graphics.vector.ImageVector

internal data class ProfileScreenRowItem(
    val id: ProfileScreenRowItemId,
    val name: String,
    val icon: ImageVector
)