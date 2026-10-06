package dev.jason.app.compose.vaultchat.main.domain

import dev.jason.app.compose.vaultchat.core.model.user.User

interface ConnectionsApiRepository {

    suspend fun getConnections(): List<User>
}