package dev.jason.app.compose.vaultchat.core

import dev.jason.app.compose.vaultchat.core.utils.OpenLinksService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreModule = module {
    singleOf(::OpenLinksService)
}