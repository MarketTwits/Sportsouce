package com.markettwits.start.presentation.album.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.start.presentation.album.presentation.store.StartAlbumStore.Intent
import com.markettwits.start.presentation.album.presentation.store.StartAlbumStore.Label
import com.markettwits.start.presentation.album.presentation.store.StartAlbumStore.Message
import com.markettwits.start.presentation.album.presentation.store.StartAlbumStore.State

class StartAlbumExecutor : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            Intent.GoBack -> publish(Label.GoBack)
        }
    }

}
