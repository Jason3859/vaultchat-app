package dev.jason.app.compose.vaultchat.feature.user

import android.util.Log
import dev.jason.app.compose.vaultchat.core.ToastController
import dev.jason.app.compose.vaultchat.core.model.user.RegisterUserDto
import dev.jason.app.compose.vaultchat.core.model.user.User

class UserApiService(
    private val repository: UserApiRepository
) {

    suspend fun searchUsers(query: String): List<User> {
        return try {
            repository.searchUser(query)
        } catch (e: Exception) {
            Log.e("UserApiService", "searchUsers: exception occurred", e)
            ToastController.showErrorOccurredToast()
            emptyList()
        }
    }
}