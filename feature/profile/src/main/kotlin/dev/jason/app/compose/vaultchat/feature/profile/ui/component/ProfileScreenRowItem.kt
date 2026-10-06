package dev.jason.app.compose.vaultchat.feature.profile.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import dev.jason.app.compose.vaultchat.feature.profile.ui.ProfileScreenRowItem

@Composable
internal fun ProfileScreenRowItem(item: ProfileScreenRowItem, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(item.icon, null)
            Text(item.name)
        }
    }
}