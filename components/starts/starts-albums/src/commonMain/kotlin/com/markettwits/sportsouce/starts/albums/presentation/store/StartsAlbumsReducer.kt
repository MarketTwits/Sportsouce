package com.markettwits.sportsouce.starts.albums.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.sportsouce.starts.albums.presentation.store.StartsAlbumsStore.*

internal object StartsAlbumsReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State = when (msg) {
        is Message.QueryChanged -> copy(filterText = msg.text)
        is Message.YearChanged -> copy(year = msg.year)
        is Message.Loaded -> copy(items = msg.items)
    }
}
