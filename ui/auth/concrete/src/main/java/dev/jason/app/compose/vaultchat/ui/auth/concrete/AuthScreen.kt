package dev.jason.app.compose.vaultchat.ui.auth.concrete

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import dev.jason.app.compose.vaultchat.ui.auth.abstractt.AbstractAuthScreen
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AuthScreen(onAuthSuccess: () -> Unit) {
    val authViewModel: AuthViewModel = koinViewModel()
    val backStack = authViewModel.backStack

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry(Route.AUTH) {
                AbstractAuthScreen(
                    onSignInClick = {
                        authViewModel.register(onAuthSuccess)
                    },
                    onNoFirebaseAuthClick = {
                        onAuthSuccess.invoke()
                    }
                )
            }

            entry(Route.LOADING) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LoadingIndicator(
                        modifier = Modifier.size(200.dp)
                    )
                    Text(
                        text = stringResource(R.string.logging_in),
                        fontSize = 20.sp
                    )
                }
            }
        }
    )
}