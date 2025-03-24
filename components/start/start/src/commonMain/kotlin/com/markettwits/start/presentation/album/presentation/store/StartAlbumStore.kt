package com.markettwits.start.presentation.album.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.start.presentation.album.presentation.store.StartAlbumStore.Intent
import com.markettwits.start.presentation.album.presentation.store.StartAlbumStore.Label
import com.markettwits.start.presentation.album.presentation.store.StartAlbumStore.State

interface StartAlbumStore : Store<Intent, State, Label> {
    data class State(
        val images: List<String>
    )

    sealed interface Intent {
        data object GoBack : Intent
    }

    sealed interface Message

    sealed interface Label {
        data object GoBack : Label
    }

}
