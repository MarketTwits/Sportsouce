package com.markettwits.sportsouce.shop.order.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.sportsouce.shop.order.presentation.store.ShopCreateOrderStore.Message
import com.markettwits.sportsouce.shop.order.presentation.store.ShopCreateOrderStore.State

object ShopCreateOrderReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.UpdateOrderState -> msg.order
            is Message.UpdateShopRecipientState -> copy(shopRecipientState = msg.shopRecipientState)
            is Message.UpdateCreateOrderStatus -> copy(shopCreateOrderState = msg.shopCreateOrderState)
            is Message.UpdateMessage -> copy(message = msg.message)
            is Message.UpdateShopOrderResultItems -> copy(shopOrderResultItems = msg.items)
        }
    }
}