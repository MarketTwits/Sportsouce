package com.markettwits.sportsouce.shop.orders.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.sportsouce.shop.orders.presentation.store.ShopUserOrdersStore.Message
import com.markettwits.sportsouce.shop.orders.presentation.store.ShopUserOrdersStore.State

object ShopUserOrdersReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.Error -> copy(
                isLoading = false,
                isSuccess = false,
                sauceError = msg.sauceError,
                items = emptyList()
            )
            is Message.Loaded -> copy(
                items = msg.items,
                isLoading = false,
                isSuccess = true,
                sauceError = null
            )
            is Message.Loading -> copy(isLoading = true, sauceError = null)
        }
    }
}