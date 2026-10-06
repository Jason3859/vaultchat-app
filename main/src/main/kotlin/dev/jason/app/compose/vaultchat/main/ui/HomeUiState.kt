package dev.jason.app.compose.vaultchat.main.ui

import dev.jason.app.compose.vaultchat.core.model.user.UserUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class HomeUiState(
    val connections: ImmutableList<UserUi> = persistentListOf(),
    val isLoading: Boolean = false,
    val searchResults: ImmutableList<UserUi> = persistentListOf(),
    val searchQuery: String = "",
    val isSearchSuccessful: Boolean = false,
    val hasRequestedSearchAtLeastOnce: Boolean = false,
) {
    companion object {
        fun forPreview(
            connections: ImmutableList<UserUi> = defaultUsers,
            hasRequestedSearchAtLeastOnce: Boolean = false,
            searchResults: ImmutableList<UserUi> = defaultUsers.subList(0, 10),
            searchQuery: String = ""
        ) = HomeUiState(
            connections = connections,
            hasRequestedSearchAtLeastOnce = hasRequestedSearchAtLeastOnce,
            searchResults = searchResults,
            searchQuery = searchQuery
        )
    }
}