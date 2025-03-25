package com.markettwits.sportsouce.shop.cart.presentation.cart.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.sportsouce.shop.cart.presentation.cart.store.ShopCartStore.Message
import com.markettwits.sportsouce.shop.cart.presentation.cart.store.ShopCartStore.State

internal object ShopCartReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.UpdateItemsInCart -> copy(items = msg.items)
            is Message.UpdateCreateOrderAvailable -> copy(isAvailable = msg.boolean)
        }
    }
}