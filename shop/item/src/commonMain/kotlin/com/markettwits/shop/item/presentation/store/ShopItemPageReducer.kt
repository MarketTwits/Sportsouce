package com.markettwits.shop.item.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.shop.item.presentation.store.ShopItemPageStore.Message
import com.markettwits.shop.item.presentation.store.ShopItemPageStore.State

object ShopItemPageReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.Failed -> copy(isLoading = false, isError = true, message = msg.message)
            is Message.Loaded -> copy(isLoading = false, isError = false, item = msg.item)
            is Message.Loading -> copy(isLoading = true, isError = false)
        }
    }
}