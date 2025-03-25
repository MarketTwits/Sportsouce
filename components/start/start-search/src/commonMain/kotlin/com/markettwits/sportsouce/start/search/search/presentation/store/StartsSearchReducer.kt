package com.markettwits.sportsouce.start.search.search.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.sportsouce.start.search.search.presentation.store.StartsSearchStore.Message
import com.markettwits.sportsouce.start.search.search.presentation.store.StartsSearchStore.State

object StartsSearchReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.Brush -> {
                if (msg.brushWithItems) copy(query = "", starts = emptyList()) else copy(query = "")
            }

            is Message.InfoFailed -> copy(isError = true, message = msg.message, isLoading = false)
            is Message.InfoLoaded -> copy(
                isError = false,
                isLoading = false,
                starts = msg.starts.items,
                searchHistory = msg.starts.searches
            )

            is Message.ChangeTextFiled -> copy(query = msg.value)
            is Message.Loading -> copy(isLoading = true)
            is Message.FilterApply -> copy(filter = msg.filter, sorted = msg.sorted)
        }
    }
}