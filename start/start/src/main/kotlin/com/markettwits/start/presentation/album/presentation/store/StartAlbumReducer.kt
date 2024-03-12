package com.markettwits.start.presentation.album.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.start.presentation.album.presentation.store.StartAlbumStore.Message
import com.markettwits.start.presentation.album.presentation.store.StartAlbumStore.State

object StartAlbumReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            else -> TODO()
        }
    }
}