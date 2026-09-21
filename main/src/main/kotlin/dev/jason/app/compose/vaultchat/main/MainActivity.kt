package dev.jason.app.compose.vaultchat.main

import android.Manifest
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dev.jason.app.compose.vaultchat.core.AppConstants
import dev.jason.app.compose.vaultchat.core.AppConstants.ACTION
import dev.jason.app.compose.vaultchat.core.AppConstants.EVENT
import dev.jason.app.compose.vaultchat.core.AppConstants.EVENT_LAUNCH_FROM_SHARE_ACTIVITY
import dev.jason.app.compose.vaultchat.core.AppConstants.EVENT_NEW_MESSAGE
import dev.jason.app.compose.vaultchat.core.AppConstants.EXTRA_KEY_OTHER_USER_UID
import dev.jason.app.compose.vaultchat.core.AppEvent
import dev.jason.app.compose.vaultchat.core.AppEvents
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.ui.theme.VaultChatTheme
import dev.jason.app.compose.vaultchat.ui.main.stateful.MainScreen

class MainActivity : ComponentActivity() {

    private val navViewModel: NavViewModel by viewModels()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            AppState.deviceOnline()
        }

        override fun onLost(network: Network) {
            AppState.deviceOffline()
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, networkCallback)

        handleIntent(intent)

        setContent {
            VaultChatTheme {
                Scaffold { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        MainScreen(
                            backStack = navViewModel.backStack,
                            onBack = navViewModel::back,
                            navigate = navViewModel::navigate,
                            onBackToHome = navViewModel::backToHome
                        )
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val event = intent.getStringExtra(EVENT)

        when (event) {
            EVENT_NEW_MESSAGE -> handleNewMessageEvent(intent)
            EVENT_LAUNCH_FROM_SHARE_ACTIVITY -> handleLaunchFromShareActivityEvent(intent)
        }
    }

    private fun handleLaunchFromShareActivityEvent(intent: Intent) {
        val action = intent.getStringExtra(ACTION) ?: return

        if (action == AppConstants.ACTION_NAVIGATE_TO_MESSAGING_SCREEN) {
            val uid = intent.getStringExtra(EXTRA_KEY_OTHER_USER_UID) ?: return

            AppEvents.sendEvent(
                AppEvent.NavEvent.NavigateToMessagingScreen(uid)
            )
        }
    }

    private fun handleNewMessageEvent(intent: Intent) {
        AppEvents.sendEvent(
            AppEvent.NavEvent.NavigateToMessagingScreen(
                intent.getStringExtra(EXTRA_KEY_OTHER_USER_UID) ?: return
            )
        )
    }
}