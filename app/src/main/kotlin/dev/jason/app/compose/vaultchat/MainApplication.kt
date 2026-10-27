package dev.jason.app.compose.vaultchat

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.datastore.preferences.preferencesDataStore
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.coreModule
import dev.jason.app.compose.vaultchat.core.notifications.MessagesNotificationChannel
import dev.jason.app.compose.vaultchat.feature.auth.ui.authModule
import dev.jason.app.compose.vaultchat.feature.messaging.messagingModule
import dev.jason.app.compose.vaultchat.feature.profile.profileModule
import dev.jason.app.compose.vaultchat.main.mainModule
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

class MainApplication : Application() {

    private val dataStore by preferencesDataStore("prefs")

    private val baseModule = module {

        singleOf(::dataStore)
        single<HttpClient> {
            HttpClient(CIO) {
                install(WebSockets)
                install(ContentNegotiation) {
                    json(
                        Json {
                            prettyPrint = true
                            isLenient = true
                            ignoreUnknownKeys = true
                        },
                    )
                }
                engine {
                    requestTimeout = 100_000
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        initKoin()
        registerActivityLifecycleCallbacks(VcActivityLifecycleCallbacks)
        MessagesNotificationChannel(this)
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@MainApplication)
            modules(
                baseModule,
                authModule,
                messagingModule,
                profileModule,
                mainModule,
                coreModule
            )
        }
    }

    private object VcActivityLifecycleCallbacks : ActivityLifecycleCallbacks {

        private var activityCount = 0

        override fun onActivityResumed(activity: Activity) {}
        override fun onActivityDestroyed(activity: Activity) {}
        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
        override fun onActivityPaused(activity: Activity) {}

        override fun onActivityStarted(activity: Activity) {
            if (activityCount == 0) {
                AppState.appInForeground()
            }
            activityCount++
        }

        override fun onActivityStopped(activity: Activity) {
            activityCount--
            if (activityCount == 0) {
                AppState.appInBackground()
            }
        }
    }
}
