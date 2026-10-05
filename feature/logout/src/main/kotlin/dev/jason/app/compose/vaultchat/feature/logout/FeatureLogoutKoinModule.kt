package dev.jason.app.compose.vaultchat.feature.logout

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val logoutFeatureModule = module {
    singleOf(::LogoutService) { createdAtStart() }
    singleOf(::LogoutRemoteRepoImpl) { bind<LogoutRemoteRepository>() }
}
