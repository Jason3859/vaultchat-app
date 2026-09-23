package dev.jason.app.compose.vaultchat.auth

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import dev.jason.app.compose.vaultchat.core.AppConstants.ACTION_START_MAIN_ACTIVITY
import dev.jason.app.compose.vaultchat.core.AppConstants.EVENT
import dev.jason.app.compose.vaultchat.core.AppConstants.EVENT_LAUNCH_FROM_AUTH_ACTIVITY
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.model.device.Device
import dev.jason.app.compose.vaultchat.core.model.user.User
import dev.jason.app.compose.vaultchat.core.ui.theme.VaultChatTheme
import dev.jason.app.compose.vaultchat.ui.auth.stateful.AuthScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class AuthActivity : ComponentActivity() {

    private val isLoading = MutableStateFlow(true)

    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermission()

        Firebase.auth.currentUser?.let { firebaseUser ->
            lifecycleScope.launch {

                val currentUser = firebaseUser.asDomainUser()
                AppState.updateCurrentUser(currentUser)

                val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
                val currentDevice = Device.getCurrentDevice(this@AuthActivity, deviceId, firebaseUser.uid)
                AppState.updateCurrentDevice(currentDevice)

                startMainActivity()
                finish()
            }
        } ?: isLoading.update { false }

        setContent {
            VaultChatTheme {
                val isLoading by isLoading.collectAsState()
                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingIndicator(
                            modifier = Modifier.size(150.dp)
                        )
                    }
                } else {
                    AuthScreenHome()
                }
            }
        }
    }

    @Composable
    private fun AuthScreenHome() {
        Surface {
            AuthScreen(
                onAuthSuccess = ::startMainActivity
            )

            Button(
                onClick = {
                    val currentUser = User(
                        uid = "CKu3zhKID9NRisxkUmrWoczAYts1",
                        displayName = "Jason Augustine",
                        profilePictureUrl = "https://lh3.googleusercontent.com/a/ACg8ocKMNmv5uWm3KG4enNW4KQUFtUh6xDxsxgcAXJZKx5ivTpCFErH5",
                        status = User.Status.Offline
                    )

                    val currentDevice =
                        Device.getCurrentDevice(this, "6aac1a03f9f6b1c1d9dcab7a", currentUser.uid)

                    AppState.apply {
                        updateCurrentUser(currentUser)
                        updateCurrentDevice(currentDevice)
                    }

                    startMainActivity()
                }
            ) {
                Text("Bypass Auth")
            }
        }
    }

    private fun startMainActivity() {
        val intent = Intent(ACTION_START_MAIN_ACTIVITY)
        intent.putExtra(EVENT, EVENT_LAUNCH_FROM_AUTH_ACTIVITY)
        startActivity(intent)
        finish()
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        }
    }

    private fun FirebaseUser.asDomainUser() =
        User(
            uid,
            displayName!!,
            photoUrl.toString().removeSuffix("=s96-c"),
            User.Status.Online
        )
}