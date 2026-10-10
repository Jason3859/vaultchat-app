package dev.jason.app.compose.vaultchat.ui.main.stateful

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dev.jason.app.compose.vaultchat.ui.main.stateful.home.HomeScreen
import dev.jason.app.compose.vaultchat.feature.messaging.ui.MessagingScreen
import dev.jason.app.compose.vaultchat.ui.main.stateful.nav.Route
import dev.jason.app.compose.vaultchat.ui.main.stateful.profile.ProfileScreen

