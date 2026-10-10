package dev.jason.app.compose.vaultchat.share

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dev.jason.app.compose.vaultchat.core.AppConstants.ACTION
import dev.jason.app.compose.vaultchat.core.AppConstants.ACTION_NAVIGATE_TO_MESSAGING_SCREEN
import dev.jason.app.compose.vaultchat.core.AppConstants.ACTION_START_MAIN_ACTIVITY
import dev.jason.app.compose.vaultchat.core.AppConstants.EVENT
import dev.jason.app.compose.vaultchat.core.AppConstants.EVENT_LAUNCH_FROM_SHARE_ACTIVITY
import dev.jason.app.compose.vaultchat.core.AppConstants.EXTRA_KEY_OTHER_USER
import dev.jason.app.compose.vaultchat.core.AppConstants.TEXT_PLAIN
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.ToastController
import dev.jason.app.compose.vaultchat.core.model.message.Message
import dev.jason.app.compose.vaultchat.core.model.user.User
import dev.jason.app.compose.vaultchat.core.model.user.UserUi
import dev.jason.app.compose.vaultchat.core.model.user.toUi
import dev.jason.app.compose.vaultchat.core.model.user.toUser
import dev.jason.app.compose.vaultchat.core.ui.theme.VaultChatTheme
import dev.jason.app.compose.vaultchat.feature.messaging.domain.MessagingApiRepository
import dev.jason.app.compose.vaultchat.main.domain.ConnectionsService
import dev.jason.app.compose.vaultchat.main.ui.component.UserItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.time.LocalDateTime

class ShareActivity : ComponentActivity() {

    private val connectionsService: ConnectionsService by inject()
    private val messagingApiRepository: MessagingApiRepository by inject()

    private val connections = mutableStateListOf<UserUi>()
    private val selectedUsers = mutableStateListOf<UserUi>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Firebase.auth.currentUser == null) {
            Toast.makeText(this, "Not logged in", Toast.LENGTH_LONG).show()
            finish()
        }

        if (getSharedText() == null) {
            Toast.makeText(this, "Invalid session", Toast.LENGTH_LONG).show()
            finish()
        }

        lifecycleScope.launch {
            connectionsService.getConnections().collect { connections ->
                this@ShareActivity.connections.addAll(connections.map(User::toUi))
            }
        }

        setContent {
            VaultChatTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(title = { Text("Share") })
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = ::sendMessage,
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Send, null)
                        }
                    }
                ) { innerPadding ->
                    ShareScreen(
                        connections = connections.toImmutableList(),
                        innerPadding = innerPadding,
                        selectedUsers = selectedUsers.toImmutableList(),
                        onUserItemClick = { user ->
                            selectedUsers.apply {
                                if (contains(user)) {
                                    remove(user)
                                } else {
                                    add(user)
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    private fun sendMessage() {

        if (selectedUsers.isEmpty()) {
            Toast.makeText(this, "No users selected", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            selectedUsers.forEach { user ->
                val currentUser = AppState.currentUser.value!!
                val message = Message(
                    from = currentUser,
                    to = user.toUser(),
                    text = getSharedText()!!,
                    timestamp = LocalDateTime.now()
                )

                messagingApiRepository.sendMessage(message)
                    .onSuccess { ToastController.showToast("Sent successfully") }
                    .onFailure { ToastController.showToast("Failed to send message") }
            }
        }.invokeOnCompletion { finish() }

        val intent = Intent(ACTION_START_MAIN_ACTIVITY)

        intent.putExtra(EVENT, EVENT_LAUNCH_FROM_SHARE_ACTIVITY)

        if (selectedUsers.size <= 1) {
            intent.putExtra(ACTION, ACTION_NAVIGATE_TO_MESSAGING_SCREEN)
            intent.putExtra(EXTRA_KEY_OTHER_USER, selectedUsers.first().toUser())
        } else {
            AppState.updateOtherUser(null)
        }

        startActivity(intent)
    }

    private fun getSharedText(): String? {
        return if (intent.action == Intent.ACTION_SEND && intent.type == TEXT_PLAIN) {
            intent.getStringExtra(Intent.EXTRA_TEXT)
        } else null
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ShareScreen(
    connections: ImmutableList<UserUi>,
    selectedUsers: ImmutableList<UserUi>,
    onUserItemClick: (UserUi) -> Unit,
    innerPadding: PaddingValues
) {
    LazyColumn(contentPadding = innerPadding + PaddingValues(12.dp)) {
        items(connections) { user ->
            UserItem(
                selected = user in selectedUsers,
                onUserClick = { onUserItemClick(it) },
                user = user,
                index = connections.indexOf(user),
                count = connections.count()
            )
            Spacer(Modifier.height(6.dp))
        }
    }
}

@Preview
@Composable
private fun ShareScreenPreview() {
    VaultChatTheme {
        ShareScreen(
            connections = List(10) { index ->
                UserUi(
                    uid = "user @$index",
                    displayName = "Name @$index",
                    profilePictureUrl = "url",
                    status = User.Status.Online
                )
            }.toImmutableList(),
            selectedUsers = List(5) { index ->
                UserUi(
                    uid = "user @$index",
                    displayName = "Name @$index",
                    profilePictureUrl = "url",
                    status = User.Status.Online
                )
            }.toImmutableList(),
            onUserItemClick = { },
            innerPadding = PaddingValues()
        )
    }
}