package dev.jason.app.compose.vaultchat.core

import dev.jason.app.compose.vaultchat.core.model.device.Device
import dev.jason.app.compose.vaultchat.core.model.user.User
import dev.jason.app.compose.vaultchat.core.model.user.UserUi
import dev.jason.app.compose.vaultchat.core.model.user.toUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Global application state holder managing user sessions, device information,
 * and app visibility/connectivity state.
 */
object AppState {

    // --- User State ---

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    private val _otherUser = MutableStateFlow<UserUi?>(null)
    val otherUser = _otherUser.asStateFlow()

    private val _isOtherUserBlocked = MutableStateFlow(false)
    val isOtherUserBlocked = _isOtherUserBlocked.asStateFlow()

    private val _isOtherUserConnected = MutableStateFlow<Boolean?>(null)
    val isOtherUserConnected = _isOtherUserConnected.asStateFlow()

    // --- Device State ---

    private val _currentDevice = MutableStateFlow<Device?>(null)
    val currentDevice = _currentDevice.asStateFlow()

    // --- Connectivity & App Lifecycle State ---

    private val _isOnline = MutableStateFlow(true)
    val isOnline = _isOnline.asStateFlow()

    private val _isAppInForeground = MutableStateFlow(false)
    val isAppInForeground = _isAppInForeground.asStateFlow()

    // --- State Mutators ---

    fun updateCurrentUser(user: User?) {
        _currentUser.value = user
    }

    fun updateOtherUser(user: UserUi?) {
        _otherUser.value = user
    }

    fun updateIsOtherUserBlocked(blocklist: List<User>) {
        val activeOtherUser = _otherUser.value?.toUser()
        _isOtherUserBlocked.value = activeOtherUser in blocklist
    }

    fun updateCurrentDevice(device: Device?) {
        _currentDevice.value = device
    }

    fun updateIsOtherUserConnected(isConnected: Boolean?) {
        _isOtherUserConnected.update { isConnected }
    }

    fun deviceOnline() {
        _isOnline.value = true
    }

    fun deviceOffline() {
        _isOnline.value = false
    }

    fun appInForeground() {
        _isAppInForeground.value = true
    }

    fun appInBackground() {
        _isAppInForeground.value = false
    }
}
