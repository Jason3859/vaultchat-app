package dev.jason.app.compose.vaultchat.feature.profile

import dev.jason.app.compose.vaultchat.feature.profile.data.BlocklistApiRepoImpl
import dev.jason.app.compose.vaultchat.feature.profile.data.DeviceApiRepoImpl
import dev.jason.app.compose.vaultchat.feature.profile.data.LogoutRemoteRepoImpl
import dev.jason.app.compose.vaultchat.feature.profile.domain.BlocklistApiRepository
import dev.jason.app.compose.vaultchat.feature.profile.domain.DeviceApiRepository
import dev.jason.app.compose.vaultchat.feature.profile.domain.LogoutRemoteRepository
import dev.jason.app.compose.vaultchat.feature.profile.domain.LogoutService
import dev.jason.app.compose.vaultchat.feature.profile.ui.ProfileViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val profileModule = module {
    singleOf(::BlocklistApiRepoImpl) { bind<BlocklistApiRepository>() }
    singleOf(::DeviceApiRepoImpl) { bind<DeviceApiRepository>() }
    singleOf(::LogoutService) { createdAtStart() }
    singleOf(::LogoutRemoteRepoImpl) { bind<LogoutRemoteRepository>() }
    viewModelOf(::ProfileViewModel)
}