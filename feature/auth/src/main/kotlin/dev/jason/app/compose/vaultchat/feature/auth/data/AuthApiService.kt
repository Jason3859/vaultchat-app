package dev.jason.app.compose.vaultchat.feature.auth.data

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dev.jason.app.compose.vaultchat.core.AppConstants.BASE_HTTP_URL
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.model.device.Device
import dev.jason.app.compose.vaultchat.core.model.user.RegisterUserDto
import dev.jason.app.compose.vaultchat.core.model.user.User
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json

class AuthApiService(
    private val context: Context,
    private val httpClient: HttpClient
) {

    @SuppressLint("HardwareIds")
    suspend fun register() {
        val firebaseUser = Firebase.auth.currentUser ?: throw IllegalStateException("firebase user is null")
        val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        val currentDevice = Device.getCurrentDevice(context, deviceId, firebaseUser.uid)
        val currentUser = User(
            uid = firebaseUser.uid,
            displayName = firebaseUser.displayName!!,
            profilePictureUrl = firebaseUser.photoUrl?.toString()?.removeSuffix("=s96-c")!!,
            status = User.Status.Online
        )

        httpClient.post("$BASE_HTTP_URL/register") {
            contentType(ContentType.Application.Json)
            setBody(RegisterUserDto.fromUserAndDevice(currentUser, currentDevice).also { println(Json.encodeToString(it)) })
        }

        AppState.apply {
            updateCurrentUser(currentUser)
            updateCurrentDevice(currentDevice)
        }
    }
}