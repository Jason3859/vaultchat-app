package dev.jason.app.compose.vaultchat.feature.auth

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dev.jason.app.compose.vaultchat.core.AppConstants.BASE_HTTP_URL
import dev.jason.app.compose.vaultchat.core.AppEvent
import dev.jason.app.compose.vaultchat.core.AppEvents
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.model.device.Device
import dev.jason.app.compose.vaultchat.core.model.user.RegisterUserDto
import dev.jason.app.compose.vaultchat.core.model.user.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
private data class RegisterUserResponse(
    // ignore above values
    val device: Device,
) {
    @Serializable
    data class Device(
        val id: String
        // ignoring the rest values. they are of no need here
    )
}

class AuthApiService(
    private val context: Context,
    private val httpClient: HttpClient
) {

    suspend fun register() {
        val firebaseUser = Firebase.auth.currentUser ?: throw IllegalStateException("firebase user is null")
        val currentDevice = Device.getCurrentDevice(context, null, firebaseUser.uid)
        val currentUser = User(
            uid = firebaseUser.uid,
            displayName = firebaseUser.displayName!!,
            profilePictureUrl = firebaseUser.photoUrl?.toString()?.removeSuffix("=s96-c")!!,
            status = User.Status.Online
        )

        val request = httpClient.post("$BASE_HTTP_URL/register") {
            contentType(ContentType.Application.Json)
            setBody(RegisterUserDto.fromUserAndDevice(currentUser, currentDevice).also { println(Json.encodeToString(it)) })
        }

        val deviceId = request.body<RegisterUserResponse>().device.id

        AppEvents.sendEvent(AppEvent.SaveCurrentDeviceId(deviceId))
        AppState.updateCurrentUser(currentUser)
        AppState.updateCurrentDevice(currentDevice.copy(id = deviceId))
    }
}