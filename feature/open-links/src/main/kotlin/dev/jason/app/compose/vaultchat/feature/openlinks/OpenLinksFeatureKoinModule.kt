package dev.jason.app.compose.vaultchat.feature.openlinks

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val openLinksFeatureModule = module {

    singleOf(::OpenLinksService)
}
