package dev.jason.app.compose.vaultchat.ui.auth.stateful

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jason.app.compose.vaultchat.feature.auth.AuthApiService
import dev.jason.app.compose.vaultchat.feature.auth.FirebaseGoogleAuthentication
import kotlinx.coroutines.launch

internal class AuthViewModel(
    private val authApiService: AuthApiService,
    private val firebaseGoogleAuthentication: FirebaseGoogleAuthentication
) : ViewModel() {

    val backStack = mutableStateListOf(AuthRoute.AUTH)

    fun register(onAuthSuccess: () -> Unit) {

        viewModelScope.launch {
            firebaseGoogleAuthentication.beginSignIn()
                ?.addOnFailureListener {
                    Log.e(
                        "AuthViewModel",
                        "registerUser: exception while signing in with google",
                        it
                    )
                }
                ?.addOnSuccessListener {
                    backStack.add(AuthRoute.LOADING)
                    backStack.remove(AuthRoute.AUTH)

                    viewModelScope.launch {
                        authApiService.register()
                        onAuthSuccess.invoke()
                    }
                }
        }
    }
}