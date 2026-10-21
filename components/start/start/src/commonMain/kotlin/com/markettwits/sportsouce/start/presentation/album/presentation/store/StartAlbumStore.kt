package com.markettwits.sportsouce.start.presentation.album.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.sportsouce.start.presentation.album.presentation.store.StartAlbumStore.Intent
import com.markettwits.sportsouce.start.presentation.album.presentation.store.StartAlbumStore.Label
import com.markettwits.sportsouce.start.presentation.album.presentation.store.StartAlbumStore.State

interface StartAlbumStore : Store<Intent, State, Label> {
    data class State(
        val images: List<String> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed interface Intent {
        data object GoBack : Intent
        data object Retry : Intent
    }

    sealed interface Message {
        data object Loading : Message
        data class Loaded(val images: List<String>) : Message
        data class Failed(val error: String) : Message
    }

    sealed interface Label {
        data object GoBack : Label
    }

}
