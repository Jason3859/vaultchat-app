package dev.jason.app.compose.vaultchat

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.datastore.preferences.preferencesDataStore
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.notifications.MessagesNotificationChannel
import dev.jason.app.compose.vaultchat.feature.auth.AuthFeatureKoinModule
import dev.jason.app.compose.vaultchat.feature.blocklist.BlocklistFeatureKoinModule
import dev.jason.app.compose.vaultchat.feature.connections.ConnectionsFeatureKoinModule
import dev.jason.app.compose.vaultchat.feature.datastore.DataStoreKoinModule
import dev.jason.app.compose.vaultchat.feature.device.DeviceFeatureKoinModule
import dev.jason.app.compose.vaultchat.feature.logout.FeatureLogoutKoinModule
import dev.jason.app.compose.vaultchat.feature.messaging.MessagingFeatureKoinModule
import dev.jason.app.compose.vaultchat.feature.open_links.OpenLinksFeatureKoinModule
import dev.jason.app.compose.vaultchat.feature.room_db.RoomDbFeatureKoinModule
import dev.jason.app.compose.vaultchat.feature.user.UserServiceKoinModule
import dev.jason.app.compose.vaultchat.ui.auth.stateful.AuthKoinModule
import dev.jason.app.compose.vaultchat.ui.main.stateful.StatefulMainKoinModule
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
                    json(Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    })
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
                AuthFeatureKoinModule,
                UserServiceKoinModule,
                ConnectionsFeatureKoinModule,
                AuthKoinModule,
                MessagingFeatureKoinModule,
                FeatureLogoutKoinModule,
                StatefulMainKoinModule,
                BlocklistFeatureKoinModule,
                RoomDbFeatureKoinModule,
                DeviceFeatureKoinModule,
                OpenLinksFeatureKoinModule,
                DataStoreKoinModule
            )
        }
    }

    private object VcActivityLifecycleCallbacks : ActivityLifecycleCallbacks {

        private var activityCount = 0

        override fun onActivityResumed(activity: Activity) {}
        override fun onActivityDestroyed(activity: Activity) {}
        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

        override fun onActivityPaused(activity: Activity) = AppState.updateOtherUser(null)

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