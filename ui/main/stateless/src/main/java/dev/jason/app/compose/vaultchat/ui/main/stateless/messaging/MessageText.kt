package dev.jason.app.compose.vaultchat.ui.main.stateless.messaging

import android.util.Patterns
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import dev.jason.app.compose.vaultchat.ui.main.action.MessagingUiAction

@Composable
internal fun MessageText(text: String, onAction: (MessagingUiAction) -> Unit) {
    val annotatedString = buildAnnotatedString {
        append(text)
        val matcher = Patterns.WEB_URL.matcher(text)
        while (matcher.find()) {
            val start = matcher.start()
            val end = matcher.end()
            val url = matcher.group()

            addStyle(
                style = SpanStyle(
                    color = Color(0xFF1E88E5),
                    textDecoration = TextDecoration.Underline
                ),
                start = start,
                end = end
            )

            addLink(
                url = LinkAnnotation.Url(
                    url = url,
                    linkInteractionListener = {
                        onAction(MessagingUiAction.OpenLink(url))
                    }
                ),
                start = start,
                end = end
            )
        }
    }

    Text(
        text = annotatedString
    )
}