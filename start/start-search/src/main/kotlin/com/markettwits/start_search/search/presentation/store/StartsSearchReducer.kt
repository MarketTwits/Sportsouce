package com.markettwits.start_search.search.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.start_search.search.presentation.store.StartsSearchStore.Message
import com.markettwits.start_search.search.presentation.store.StartsSearchStore.State

object StartsSearchReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.Brush -> copy(query = "")
            is Message.InfoFailed -> copy(isError = true, message = msg.message)
            is Message.InfoLoaded -> copy(
                isError = false,
                starts = msg.starts.items,
                searchHistory = msg.starts.searches
            )

            is Message.ChangeTextFiled -> copy(query = msg.value)
        }
    }
}