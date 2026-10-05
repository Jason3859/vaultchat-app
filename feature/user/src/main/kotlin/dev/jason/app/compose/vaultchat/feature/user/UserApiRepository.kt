package dev.jason.app.compose.vaultchat.feature.user

import dev.jason.app.compose.vaultchat.core.model.user.User

interface UserApiRepository {

    suspend fun searchUser(displayName: String): List<User>
}