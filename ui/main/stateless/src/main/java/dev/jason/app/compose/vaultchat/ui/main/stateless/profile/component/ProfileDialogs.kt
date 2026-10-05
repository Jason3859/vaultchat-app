package dev.jason.app.compose.vaultchat.ui.main.stateless.profile.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.TabletAndroid
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.jason.app.compose.vaultchat.core.AppEvent
import dev.jason.app.compose.vaultchat.core.AppEvents
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.model.device.Device
import dev.jason.app.compose.vaultchat.core.model.device.DeviceUi
import dev.jason.app.compose.vaultchat.core.model.device.toDevice
import dev.jason.app.compose.vaultchat.core.model.user.UserUi
import dev.jason.app.compose.vaultchat.core.ui.LoadProfilePicture
import dev.jason.app.compose.vaultchat.ui.main.stateless.R
import dev.jason.app.compose.vaultchat.ui.main.stateless.profile.action.ProfileUiAction
import dev.jason.app.compose.vaultchat.ui.main.stateless.profile.model.ProfileScreenDialogId
import dev.jason.app.compose.vaultchat.ui.main.stateless.profile.state.ProfileUiState
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun ProfileDialogs(
    uiState: ProfileUiState,
    onBack: () -> Unit,
    onAction: (ProfileUiAction) -> Unit
) {
    val user = uiState.user!!
    val onDismiss = { onAction(ProfileUiAction.DismissCurrentDialog) }

    when (uiState.currentDialog) {
        ProfileScreenDialogId.Devices -> DevicesDialog(
            devices = uiState.devices,
            onAction = onAction,
            onDismiss = onDismiss
        )

        ProfileScreenDialogId.Blocklist -> BlocklistDialog(
            blocklist = uiState.blocklist,
            onDismiss = onDismiss
        )

        ProfileScreenDialogId.Logout -> LogoutDialog(
            onDismiss = onDismiss,
            onAction = onAction
        )

        ProfileScreenDialogId.BlockUser -> BlockUserDialog(
            userDisplayName = user.displayName,
            onConfirmClick = {
                onAction(
                    ProfileUiAction.BlockUser(
                        user = user,
                        onFinish = {
                            AppEvents.sendEvent(AppEvent.NavEvent.NavigateToHomeScreen)
                        }
                    )
                )
                onDismiss()
            },
            onDismiss = onDismiss
        )

        ProfileScreenDialogId.DeleteAllMessages -> DeleteMessagesHistoryDialog(
            displayName = user.displayName,
            onDismiss = onDismiss,
            onConfirmClick = {
                onAction(ProfileUiAction.DeleteMessagesHistory)
                onDismiss.invoke()
                onBack.invoke()
            }
        )

        ProfileScreenDialogId.UnblockUser -> UnblockUserDialog(
            userDisplayName = user.displayName,
            onConfirmClick = {
                onAction(ProfileUiAction.UnblockUser(user, onDismiss))
                onDismiss()
                AppEvents.sendEvent(AppEvent.NavEvent.NavigateToHomeScreen)
            },
            onDismiss = onDismiss
        )

        null -> {}
    }
}


@Composable
fun DeleteMessagesHistoryDialog(
    displayName: String,
    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.delete_all_messages_warning_title, displayName)) },
        text = { Text(stringResource(R.string.delete_all_messages_warning_text)) },
        confirmButton = {
            TextButton(
                onClick = onConfirmClick,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(stringResource(R.string.delete_all_messages))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
private fun BlockUserDialog(
    userDisplayName: String,
    onConfirmClick: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.block_user_warning_title, userDisplayName)) },
        text = { Text(stringResource(R.string.block_user_warning_text)) },
        confirmButton = {
            TextButton(
                onClick = onConfirmClick,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(stringResource(R.string.block))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
private fun UnblockUserDialog(
    userDisplayName: String,
    onConfirmClick: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.unblock_user_warning_title, userDisplayName)) },
        text = { Text(stringResource(R.string.unblock_user_warning_text)) },
        confirmButton = {
            TextButton(
                onClick = onConfirmClick,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(stringResource(R.string.unblock))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
private fun LogoutDialog(
    onDismiss: () -> Unit,
    onAction: (ProfileUiAction) -> Unit
) {
    val showConfirmDialog = remember { mutableStateOf(false) }

    if (!showConfirmDialog.value) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(stringResource(R.string.logout_warning_title)) },
            confirmButton = {
                TextButton(
                    onClick = { showConfirmDialog.value = true },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(stringResource(R.string.logout))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    } else {
        AlertDialog(
            onDismissRequest = {
                showConfirmDialog.value = false
                onDismiss()
            },
            confirmButton = {
                TextButton(
                    onClick = { onAction(ProfileUiAction.LogoutCurrentDeviceByClearingMessages) },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(stringResource(R.string.logout_by_clearing_messages))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onAction(ProfileUiAction.LogoutCurrentDeviceWithoutClearingMessages) },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(stringResource(R.string.logout_without_clearing_messages))
                }
            },
            title = { Text(stringResource(R.string.logout_clear_messages_title)) },
            text = { Text(stringResource(R.string.logout_clear_messages_text)) }
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun BlocklistDialog(
    blocklist: ImmutableList<UserUi>,
    onDismiss: () -> Unit
) {
    val imageModifier = Modifier
        .size(30.dp)
        .clip(CircleShape)

    Dialog(onDismissRequest = onDismiss) {
        LazyColumn {
            if (blocklist.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(R.string.no_blocked_users))
                    }
                }
            }
            itemsIndexed(blocklist) { index, user ->
                SegmentedListItem(
                    onClick = {},
                    shapes = ListItemDefaults.segmentedShapes(
                        index = index,
                        count = blocklist.count(),
                    ),
                    leadingContent = {
                        LoadProfilePicture(
                            url = user.profilePictureUrl,
                            modifier = imageModifier
                        )
                    }
                ) {
                    Text(user.displayName)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun DevicesDialog(
    devices: ImmutableList<DeviceUi>,
    onAction: (ProfileUiAction) -> Unit,
    onDismiss: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showClearMessagesInDeviceDialog by remember { mutableStateOf(false) }
    var deviceToLogout by remember { mutableStateOf<DeviceUi?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Column {
            if (showLogoutDialog) {
                AlertDialog(
                    onDismissRequest = {
                        deviceToLogout = null
                        showLogoutDialog = false
                    },
                    confirmButton = {
                        TextButton(
                            onClick = { showClearMessagesInDeviceDialog = true },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text(stringResource(R.string.logout))
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                deviceToLogout = null
                                showLogoutDialog = false
                            }
                        ) {
                            Text(stringResource(R.string.cancel))
                        }
                    },
                    title = { Text(stringResource(R.string.device_logout_warning_title)) },
                    text = { Text(stringResource(R.string.device_logout_warning_text)) }
                )
            }

            if (showClearMessagesInDeviceDialog) {
                val deviceToLogoutSafe =
                    deviceToLogout ?: throw IllegalStateException("Device to logout is null")

                AlertDialog(
                    onDismissRequest = onDismiss,
                    title = { Text(stringResource(R.string.logout_clear_messages_title)) },
                    text = { Text(stringResource(R.string.logout_clear_messages_text)) },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                onAction(
                                    ProfileUiAction.LogoutDeviceByClearingMessages(
                                        deviceToLogoutSafe
                                    )
                                )
                                onDismiss()
                            },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text(stringResource(R.string.logout_by_clearing_messages))
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                onAction(
                                    ProfileUiAction.LogoutDeviceWithoutClearingMessages(
                                        deviceToLogoutSafe
                                    )
                                )
                                onDismiss()
                            },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text(stringResource(R.string.logout_without_clearing_messages))
                        }
                    }
                )
            }

            LazyColumn {
                item {
                    Text(
                        text = stringResource(R.string.your_devices),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                itemsIndexed(devices) { index, device ->
                    val isCurrentDevice = device.toDevice() == AppState.currentDevice.collectAsState().value

                    SegmentedListItem(
                        shapes = ListItemDefaults.segmentedShapes(
                            index = index,
                            count = devices.count(),
                        ),
                        onClick = {
                            if (!isCurrentDevice) {
                                deviceToLogout = device
                                showLogoutDialog = true
                            }
                        },
                        leadingContent = {
                            val icon = when (device.type) {
                                Device.Type.Mobile -> Icons.Default.PhoneAndroid
                                Device.Type.Tablet -> Icons.Default.TabletAndroid
                            }
                            Icon(icon, null)
                        }
                    ) {
                        var text = device.name
                        if (isCurrentDevice) text += " (current)"

                        Text(text)
                    }
                }
            }
        }
    }
}