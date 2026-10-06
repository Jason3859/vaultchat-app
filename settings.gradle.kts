@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "VaultChat"

include(":app")
include(":core")
include(":auth")
include(":main")
include(":share")

include(":feature")
include(":feature:profile")
include(":feature:logout")
include(":feature:device")
include(":feature:connections")
include(":feature:user")
include(":feature:messaging")
include(":feature:blocklist")
include(":feature:auth")

include(":ui")
include(":ui:auth")
include(":ui:main")
include(":ui:main:stateless")
include(":ui:main:stateful")
