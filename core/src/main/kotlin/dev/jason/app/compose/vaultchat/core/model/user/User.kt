package dev.jason.app.compose.vaultchat.core.model.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val uid: String,
    val displayName: String,
    val profilePictureUrl: String,
    val status: Status
) : Parcelable {
    enum class Status {
        Online, Offline
    }

    override fun equals(other: Any?): Boolean {
        if (other !is User) return false
        return other.uid == uid
    }

    override fun hashCode(): Int {
        var result = uid.hashCode()
        result = 31 * result + displayName.hashCode()
        result = 31 * result + profilePictureUrl.hashCode()
        result = 31 * result + status.hashCode()
        return result
    }
}