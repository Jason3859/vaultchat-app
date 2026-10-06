package dev.jason.app.compose.vaultchat.feature.profile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.model.device.Device
import dev.jason.app.compose.vaultchat.core.model.device.DeviceUi
import dev.jason.app.compose.vaultchat.core.model.device.toUi
import dev.jason.app.compose.vaultchat.core.model.user.User
import dev.jason.app.compose.vaultchat.core.model.user.UserUi
import dev.jason.app.compose.vaultchat.core.model.user.toUi
import dev.jason.app.compose.vaultchat.core.model.user.toUser
import dev.jason.app.compose.vaultchat.feature.messaging.domain.MessageDatabaseRepository
import dev.jason.app.compose.vaultchat.feature.profile.domain.BlocklistApiRepository
import dev.jason.app.compose.vaultchat.feature.profile.domain.DeviceApiRepository
import dev.jason.app.compose.vaultchat.feature.profile.domain.LogoutService
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val logoutService: LogoutService,
    private val deviceApiRepository: DeviceApiRepository,
    private val blocklistApiRepository: BlocklistApiRepository,
    private val messageDatabaseRepository: MessageDatabaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()
        .onStart {
            fetchBlocklist()
            fetchDevices()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2500),
            initialValue = ProfileUiState()
        )

    init {
        loadUserProfile()

        viewModelScope.launch {
            AppState.isOtherUserBlocked.collect {
                _uiState.update { state ->
                    state.copy(
                        isOtherUserBlocked = it
                    )
                }
            }
        }
    }

    fun onAction(action: ProfileUiAction) {
        when (action) {
            is ProfileUiAction.ShowDialog -> showDialog(action.id)
            ProfileUiAction.DismissCurrentDialog -> dismissCurrentDialog()

            is ProfileUiAction.BlockUser -> blockUser(action.user, action.onFinish)
            is ProfileUiAction.UnblockUser -> unblockUser(action.user, action.onFinish)

            ProfileUiAction.LogoutCurrentDeviceByClearingMessages -> logoutCurrentDevice(clearMessages = true)
            ProfileUiAction.LogoutCurrentDeviceWithoutClearingMessages -> logoutCurrentDevice(clearMessages = false)

            is ProfileUiAction.LogoutDeviceByClearingMessages -> logoutDevice(action.device, clearMessages = true)
            is ProfileUiAction.LogoutDeviceWithoutClearingMessages -> logoutDevice(action.device, clearMessages = false)

            ProfileUiAction.DeleteMessagesHistory -> deleteMessagesHistory()
        }
    }

    // Dialog Management

    private fun showDialog(id: ProfileScreenDialogId) {
        _uiState.update { it.copy(currentDialog = id) }
    }

    private fun dismissCurrentDialog() {
        _uiState.update { it.copy(currentDialog = null) }
    }

    // User Profile & Blocklist

    private fun loadUserProfile() {
        val currentUser = AppState.currentUser.value?.toUi()
        val otherUser = AppState.otherUser.value?.toUi()
        val isOwnProfile = (otherUser == null)
        val profileUser = otherUser ?: currentUser ?: return

        _uiState.update { state ->
            state.copy(
                user = profileUser,
                isOwnProfile = isOwnProfile
            )
        }
    }

    private fun fetchBlocklist() {
        viewModelScope.launch {
            val blocklist = blocklistApiRepository.getBlocklist()
                .map(User::toUi)
                .toImmutableList()

            _uiState.update { state ->
                AppState.updateIsOtherUserBlocked(blocklist.map(UserUi::toUser))
                state.copy(
                    blocklist = blocklist
                )
            }
        }
    }

    private fun blockUser(user: UserUi, onFinish: () -> Unit) {
        viewModelScope.launch {
            blocklistApiRepository.blockUser(user.toUser())
            fetchBlocklist()
            onFinish()
        }
    }

    private fun unblockUser(user: UserUi, onFinish: () -> Unit) {
        viewModelScope.launch {
            blocklistApiRepository.unblockUser(user.toUser())
            fetchBlocklist()
            onFinish()
        }
    }

    // Device Management & Logout

    private fun fetchDevices() {
        viewModelScope.launch {
            val devices = deviceApiRepository.getDevices()
                .map(Device::toUi)
                .toImmutableList()

            _uiState.update { it.copy(devices = devices) }
        }
    }

    private fun logoutCurrentDevice(clearMessages: Boolean) {
        viewModelScope.launch {
            logoutService.logoutCurrentDevice(clearMessages = clearMessages)
        }
    }

    private fun logoutDevice(device: DeviceUi, clearMessages: Boolean) {
        val deviceId = device.id ?: return
        viewModelScope.launch {
            logoutService.sendLogoutRequest(deviceId, clearMessages)
        }
    }

    // Message History

    private fun deleteMessagesHistory() {
        viewModelScope.launch {
            val currentUser = AppState.currentUser.value ?: return@launch
            val otherUser = AppState.otherUser.value ?: return@launch

            messageDatabaseRepository.deleteMessageHistory(currentUser.uid, otherUser.uid)
        }
    }
}
