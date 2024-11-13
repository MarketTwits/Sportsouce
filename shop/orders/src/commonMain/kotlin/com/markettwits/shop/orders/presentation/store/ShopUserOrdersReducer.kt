package com.markettwits.shop.orders.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.shop.orders.presentation.store.ShopUserOrdersStore.State
import com.markettwits.shop.orders.presentation.store.ShopUserOrdersStore.Message

object ShopUserOrdersReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            else -> TODO()
        }
    }
}