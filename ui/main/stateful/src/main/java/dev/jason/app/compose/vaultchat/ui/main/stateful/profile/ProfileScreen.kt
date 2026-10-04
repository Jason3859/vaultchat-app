package dev.jason.app.compose.vaultchat.ui.main.stateful.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.model.user.toUi
import dev.jason.app.compose.vaultchat.ui.main.stateless.profile.component.AbstractProfileScreen
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    onBack: () -> Unit
) {
    val viewModel: ProfileViewModel = koinViewModel()

    val devices by viewModel.devices.collectAsStateWithLifecycle()
    val blocklist by viewModel.blocklist.collectAsStateWithLifecycle()

    val currentUser by AppState.currentUser.collectAsState()
    val otherUser by AppState.otherUser.collectAsState()

    if (otherUser == null)
        AbstractProfileScreen(
            onBack = onBack,
            user = currentUser?.toUi()!!,
            devices = devices.toImmutableList(),
            blocklist = blocklist.toImmutableList(),
            onAction = viewModel::onAction
        )
    else
        AbstractProfileScreen(
            onBack = onBack,
            user = otherUser?.toUi()!!,
            isOtherUserBlocked = otherUser?.toUi() in blocklist,
            onAction = viewModel::onAction
        )
}