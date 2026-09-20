package dev.jason.app.compose.vaultchat.core.model.device

import androidx.compose.runtime.Immutable

@Immutable
data class DeviceUi(
    val id: String? = null,
    val ownerUid: String,
    val name: String,
    val type: Device.Type,
    val os: Device.Os,
    val version: String,
)

fun Device.toUi() = DeviceUi(
    id = id,
    ownerUid = ownerUid,
    name = name,
    type = type,
    os = os,
    version = version,
)

fun DeviceUi.toDevice() = Device(
    id = id,
    ownerUid = ownerUid,
    name = name,
    type = type,
    os = os,
    version = version,
)