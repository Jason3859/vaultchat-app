package dev.jason.app.compose.vaultchat.feature.user

import dev.jason.app.compose.vaultchat.core.AppConstants.BASE_HTTP_URL
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.model.user.User
import dev.jason.app.compose.vaultchat.core.model.user.UserDto
import dev.jason.app.compose.vaultchat.core.model.user.toUser
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

internal class UserApiRepoImpl(
    private val client: HttpClient
) : UserApiRepository {

    override suspend fun searchUser(displayName: String): List<User> {
        val response = client.get("$BASE_HTTP_URL/user/search") {
            parameter("from_uid", AppState.currentUser.value?.uid!!)
            parameter("search_query", displayName)
        }

        return response.body<List<UserDto>>()
            .map(UserDto::toUser)
    }
}