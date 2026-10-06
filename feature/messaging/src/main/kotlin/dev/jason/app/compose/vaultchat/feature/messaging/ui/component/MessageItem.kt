package dev.jason.app.compose.vaultchat.feature.messaging.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.jason.app.compose.vaultchat.core.model.message.MessageUi
import dev.jason.app.compose.vaultchat.feature.messaging.ui.MessageStatus
import dev.jason.app.compose.vaultchat.feature.messaging.ui.MessagingUiAction

@Composable
internal fun MessageItem(
    msg: MessageUi,
    isOtherUser: Boolean,
    status: MessageStatus,
    onAction: (MessagingUiAction) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isOtherUser) Alignment.Start else Alignment.End
    ) {
        Card(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(top = 6.dp),
            colors = CardDefaults.cardColors(
                containerColor = when (status) {
                    MessageStatus.Pending -> Color.Cyan
                    MessageStatus.Failed -> MaterialTheme.colorScheme.errorContainer
                    MessageStatus.Sent -> Color.Unspecified // default color
                }
            )
        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                MessageText(msg.text, onAction)
            }
        }

        Text(
            text = when (status) {
                MessageStatus.Pending -> "Sending"
                MessageStatus.Failed -> "Failed"
                MessageStatus.Sent -> msg.timestamp
            },
            fontSize = 10.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}
