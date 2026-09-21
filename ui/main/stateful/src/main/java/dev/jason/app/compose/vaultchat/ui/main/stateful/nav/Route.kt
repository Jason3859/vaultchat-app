package dev.jason.app.compose.vaultchat.ui.main.stateful.nav

import androidx.navigation3.runtime.NavKey
import dev.jason.app.compose.vaultchat.core.model.user.User
import dev.jason.app.compose.vaultchat.core.model.user.UserUi
import kotlinx.serialization.Serializable

sealed interface Route : NavKey {

    @Serializable
    data object Home : Route

    @Serializable
    data class Messaging(
        val uid: String,
        val displayName: String? = null,
        val profilePictureUrl: String? = null,
        val status: User.Status? = null
    ) : Route {
        companion object {
            fun fromUser(user: UserUi) =
                Messaging(user.uid, user.displayName, user.profilePictureUrl, user.status)
        }

        fun asUserUi(): UserUi {
            return if (displayName == null) UserUi(uid) else UserUi(
                uid,
                displayName,
                profilePictureUrl!!,
                status!!
            )
        }
    }

    @Serializable
    data object Profile : Route
}