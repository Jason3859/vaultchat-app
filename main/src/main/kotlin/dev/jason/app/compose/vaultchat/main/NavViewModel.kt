package dev.jason.app.compose.vaultchat.main

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jason.app.compose.vaultchat.core.AppEvent
import dev.jason.app.compose.vaultchat.core.AppEvents
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.model.user.toUi
import dev.jason.app.compose.vaultchat.main.ui.Route
import kotlinx.coroutines.launch

class NavViewModel : ViewModel() {

    val backStack = mutableStateListOf<Route>(Route.Home)

    fun navigate(route: Route) {
        if (route is Route.Messaging) {
            if (backStack.last() is Route.Messaging) {
                backStack.removeLastOrNull()
            }
        }
        backStack.add(route)
    }

    fun back() {
        if (backStack.last() is Route.Messaging) {
            AppState.updateOtherUser(null)
            AppState.updateIsOtherUserConnected(null)
        }

        if (backStack.last() !is Route.Home) {
            backStack.removeLastOrNull()
        }
    }

    fun backToHome() {
        while (backStack.last() !is Route.Home) {
            back()
        }
    }

    init {
        viewModelScope.launch {
            AppEvents.events.collect { event ->
                if (event is AppEvent.NavEvent) {
                    when (event) {
                        is AppEvent.NavEvent.NavigateToMessagingScreen -> {
                            AppState.apply {
                                updateOtherUser(event.user.toUi())
                                updateIsOtherUserConnected(true)
                            }
                            navigate(Route.Messaging(event.user.uid))
                        }
                        is AppEvent.NavEvent.NavigateToHomeScreen -> {
                            backToHome()
                        }
                    }
                }
            }
        }
    }
}