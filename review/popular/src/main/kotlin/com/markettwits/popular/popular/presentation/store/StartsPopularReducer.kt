package com.markettwits.popular.popular.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.popular.popular.presentation.store.StartsPopularStore.Message
import com.markettwits.popular.popular.presentation.store.StartsPopularStore.State

internal object StartsPopularReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.Failed -> copy(isLoading = false, isError = true, message = msg.message)
            is Message.Loaded -> copy(isLoading = false, isError = false, starts = msg.starts)
            is Message.Loading -> copy(isLoading = true)
        }
    }
}