package dev.jason.app.compose.vaultchat.feature.auth.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import dev.jason.app.compose.vaultchat.core.ui.DeviceType
import dev.jason.app.compose.vaultchat.core.ui.LoadingIndicator
import dev.jason.app.compose.vaultchat.core.ui.theme.VaultChatTheme
import dev.jason.app.compose.vaultchat.feature.auth.R
import dev.jason.app.compose.vaultchat.feature.auth.ui.component.CompactAuthScreen
import dev.jason.app.compose.vaultchat.feature.auth.ui.component.LargeAuthScreen
import dev.jason.app.compose.vaultchat.feature.auth.ui.component.MediumAuthScreen
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AuthScreen(onAuthSuccess: () -> Unit) {
    val authViewModel: AuthViewModel = koinViewModel()
    val backStack = authViewModel.backStack
    val context = LocalContext.current

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry(AuthRoute.AUTH) {
                AuthScreen(
                    onSignInClick = {
                        authViewModel.register(context, onAuthSuccess)
                    }
                )
            }

            entry(AuthRoute.LOADING) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LoadingIndicator()
                    Text(
                        text = stringResource(R.string.logging_in),
                        fontSize = 20.sp
                    )
                }
            }
        }
    )
}

@Composable
fun AuthScreen(
    onSignInClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val deviceType = DeviceType.rememberWindowSize()

    when (deviceType) {
        is DeviceType.Compact -> CompactAuthScreen(onSignInClick, modifier)
        is DeviceType.Medium -> MediumAuthScreen(onSignInClick, modifier)
        is DeviceType.Foldable -> LargeAuthScreen(onSignInClick, modifier)
        is DeviceType.Expanded -> LargeAuthScreen(onSignInClick, modifier)
        is DeviceType.Large -> LargeAuthScreen(onSignInClick, modifier)
        is DeviceType.ExtraLarge -> LargeAuthScreen(onSignInClick, modifier)
    }
}

@PreviewScreenSizes
@Composable
private fun AuthScreenPreview() {
    VaultChatTheme {
        AuthScreen(
            onSignInClick = {}
        )
    }
}