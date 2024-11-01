package com.markettwits.shop.order.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStore.State
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStore.Message

object ShopCreateOrderReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.UpdateOrderState -> msg.order
        }
    }
}