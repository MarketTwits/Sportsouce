package com.markettwits.shop.cart.presentation.cart.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.shop.cart.presentation.cart.store.ShopCartStore.State
import com.markettwits.shop.cart.presentation.cart.store.ShopCartStore.Message

internal object ShopCartReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.UpdateItemsInCart -> copy(items = msg.items)
            is Message.UpdateOrder -> copy(order = msg.order)
        }
    }
}