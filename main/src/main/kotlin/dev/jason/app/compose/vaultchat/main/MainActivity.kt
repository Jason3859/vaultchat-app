package dev.jason.app.compose.vaultchat.main

import android.Manifest
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dev.jason.app.compose.vaultchat.core.AppConstants.ACTION_MAIN_ACTIVITY_CLEAR_OTHER_USER
import dev.jason.app.compose.vaultchat.core.AppConstants.EXTRA_NAV_DESTINATION_KEY
import dev.jason.app.compose.vaultchat.core.AppConstants.SHARE_ACTIVITY_LAUNCH_MAIN_ACTIVITY_ACTION
import dev.jason.app.compose.vaultchat.core.AppEvent
import dev.jason.app.compose.vaultchat.core.AppEvents
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.model.device.Device
import dev.jason.app.compose.vaultchat.core.model.user.User
import dev.jason.app.compose.vaultchat.core.ui.theme.VaultChatTheme
import dev.jason.app.compose.vaultchat.ui.main.concrete.MainScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val navViewModel: NavViewModel by viewModels()

    private val firebaseUser = Firebase.auth.currentUser
    private var currentUser = firebaseUser?.let {
        User(
            firebaseUser.uid,
            firebaseUser.displayName!!,
            firebaseUser.photoUrl.toString().removeSuffix("=s96-c"),
            User.Status.Online
        )
    }

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

        lifecycleScope.launch {
            
            if (currentUser == null) {
                currentUser = User(
                    uid = "CKu3zhKID9NRisxkUmrWoczAYts1",
                    displayName = "Jason Augustine",
                    profilePictureUrl = "https://lh3.googleusercontent.com/a/ACg8ocKMNmv5uWm3KG4enNW4KQUFtUh6xDxsxgcAXJZKx5ivTpCFErH5=s96-c",
                    status = User.Status.Online
                )
            }
            
            AppState.updateCurrentUser(currentUser!!)
            AppState.updateCurrentDevice(
                Device.getCurrentDevice(this@MainActivity, currentUser?.uid!!)
            )
        }

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
        val navigateToMessagingScreenString = intent.getStringExtra(EXTRA_NAV_DESTINATION_KEY)
        if (navigateToMessagingScreenString != null) {
            AppEvents.sendEvent(
                AppEvent.NavEvent.NavigateToMessagingScreen(
                    intent.getStringExtra("uid") ?: return // received from notification
                )
            )
            return
        }

        val stringFromReceivedIntent = intent.getStringExtra(SHARE_ACTIVITY_LAUNCH_MAIN_ACTIVITY_ACTION)

        if (stringFromReceivedIntent == ACTION_MAIN_ACTIVITY_CLEAR_OTHER_USER) {
            AppState.updateOtherUser(null)
        } else {
            // here, value of `stringFromReceivedIntent` will be the uid of user

            // send event to navigate to the screen
            // with the uid of the user that current user
            // is wanting to send something to.
            AppEvents.sendEvent(
                AppEvent.NavEvent.NavigateToMessagingScreen(
                    stringFromReceivedIntent ?: run {
                        Log.w("MainActivity", "invalid intent received")
                        return
                    }
                )
            )
        }
    }
}