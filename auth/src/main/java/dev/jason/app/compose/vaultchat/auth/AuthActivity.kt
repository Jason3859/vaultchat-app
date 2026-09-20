package dev.jason.app.compose.vaultchat.auth

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import dev.jason.app.compose.vaultchat.feature.datastore.device_id.DeviceIdDataStoreRepository
import dev.jason.app.compose.vaultchat.ui.auth.stateful.AuthScreen
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class AuthActivity : ComponentActivity() {

    private val deviceIdDataStoreRepository: DeviceIdDataStoreRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermission()

        Firebase.auth.currentUser?.let { firebaseUser ->
            lifecycleScope.launch {

                val currentUser = firebaseUser.asDomainUser()
                AppState.updateCurrentUser(currentUser)

                val deviceId = deviceIdDataStoreRepository.getCurrentDeviceId()
                    ?: throw IllegalStateException("Device id is null")

                val currentDevice = Device.getCurrentDevice(this@AuthActivity, deviceId, firebaseUser.uid)
                AppState.updateCurrentDevice(currentDevice)

                startMainActivity()
            }
        }

        setContent {
            VaultChatTheme {
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

                            val currentDevice = Device.getCurrentDevice(this, "6aac1a03f9f6b1c1d9dcab7a", currentUser.uid)

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