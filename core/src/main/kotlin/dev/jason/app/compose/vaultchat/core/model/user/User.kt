package dev.jason.app.compose.vaultchat.core.model.user

data class User(
    val uid: String,
    val displayName: String?, // TODO: remove null
    val profilePictureUrl: String?,
    val status: Status?
) {
    enum class Status {
        Online, Offline
    }

    companion object {
        fun emptyUserWithUid(uid: String) = User(uid, null, null, null)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is User) return false
        return other.uid == uid
    }

    override fun hashCode(): Int {
        var result = uid.hashCode()
        result = 31 * result + (displayName?.hashCode() ?: 0)
        result = 31 * result + (profilePictureUrl?.hashCode() ?: 0)
        result = 31 * result + (status?.hashCode() ?: 0)
        return result
    }
}