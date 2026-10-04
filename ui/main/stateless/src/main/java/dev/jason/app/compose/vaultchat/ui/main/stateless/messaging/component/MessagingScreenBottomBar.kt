package dev.jason.app.compose.vaultchat.ui.main.stateless.messaging.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import dev.jason.app.compose.vaultchat.ui.main.stateless.R
import dev.jason.app.compose.vaultchat.ui.main.stateless.messaging.action.MessagingUiAction
import dev.jason.app.compose.vaultchat.ui.main.stateless.messaging.state.MessagingUiState

@Composable
internal fun MessagingScreenBottomBar(
    uiState: MessagingUiState,
    onAction: (MessagingUiAction) -> Unit
) {
    BottomAppBar(
        containerColor = Color.Transparent
    ) {
        if (!uiState.isUserBlocked) {
            OutlinedTextField(
                value = uiState.messageText,
                onValueChange = { newValue ->
                    onAction(MessagingUiAction.UpdateState {
                        it.copy(messageText = newValue)
                    })
                },
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .padding(8.dp)
                    .height(55.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                shape = CircleShape,
                placeholder = { Text("Message") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = { onAction(MessagingUiAction.SendMessage) }
                )
            )

            IconButton(
                onClick = { onAction(MessagingUiAction.SendMessage) },
                colors = IconButtonDefaults.filledTonalIconButtonColors(),
                shape = CircleShape,
                modifier = Modifier.size(55.dp),
                enabled = uiState.sendButtonEnabled
            ) {
                Icon(Icons.AutoMirrored.Default.Send, null)
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(stringResource(R.string.user_blocked_warning))
            }
        }
    }
}