package com.markettwits.sportsouce.shop.item.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.sportsouce.shop.item.presentation.store.ShopItemPageStore.Message
import com.markettwits.sportsouce.shop.item.presentation.store.ShopItemPageStore.State

object ShopItemPageReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.Failed -> copy(isLoading = false, isError = true, message = msg.message)
            is Message.Loaded -> copy(
                isLoading = false,
                isError = false,
                shopItem = msg.item,
                shopItemOptions = msg.shopItemOptions
            )

            is Message.Loading -> copy(isLoading = true, isError = false)
        }
    }
}