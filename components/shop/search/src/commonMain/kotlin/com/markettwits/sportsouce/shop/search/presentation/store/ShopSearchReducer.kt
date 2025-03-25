package com.markettwits.sportsouce.shop.search.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.sportsouce.shop.search.presentation.store.ShopSearchStore.Message
import com.markettwits.sportsouce.shop.search.presentation.store.ShopSearchStore.State


object ShopSearchReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.QueryWasUpdated -> copy(query = msg.query)
            is Message.ShowHistoryItems -> copy(historyItems = msg.items)
        }
    }
}