package dev.jason.app.compose.vaultchat.main.ui

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.feature.messaging.ui.MessagingScreen
import dev.jason.app.compose.vaultchat.feature.profile.ui.ProfileScreen

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MainScreen(
    backStack: SnapshotStateList<Route>,
    onBack: () -> Unit,
    navigate: (Route) -> Unit,
    onBackToHome: () -> Unit
) {

    NavDisplay(
        backStack = backStack,
        onBack = onBack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        sceneStrategies = listOf(rememberListDetailSceneStrategy()),
        entryProvider = entryProvider {
            entry<Route.Home>(
                metadata = ListDetailSceneStrategy.listPane()
            ) {
                HomeScreen(
                    onUserClick = { user ->
                        navigate(Route.Messaging(user.uid))
                        AppState.updateOtherUser(user)
                        AppState.updateIsOtherUserConnected(true)
                    },
                    onNewUserClick = { user ->
                        navigate(Route.Messaging(user.uid))
                        AppState.updateOtherUser(user)
                        AppState.updateIsOtherUserConnected(false)
                    },
                    onProfileClick = { navigate(Route.Profile) }
                )
            }

            entry<Route.Messaging>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) {
                MessagingScreen(
                    onBackClick = onBackToHome,
                    onUserInfoClick = {
                        navigate(Route.Profile)
                    }
                )
            }

            entry<Route.Profile>(
                metadata = ListDetailSceneStrategy.extraPane()
            ) {
                ProfileScreen(
                    onBack = onBack
                )
            }
        }
    )
}