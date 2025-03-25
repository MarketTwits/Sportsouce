package com.markettwits.sportsouce.profile.registrations.detail.store.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.sportsouce.profile.registrations.detail.store.store.StartOrderStore.Message
import com.markettwits.sportsouce.profile.registrations.detail.store.store.StartOrderStore.State

object StartOrderStoreReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.GetPriceDontRequired -> copy(
                startPriceResult = StartOrderStore.StartPriceResult.Free
            )

            is Message.GetPriceFailed -> copy(
                startPriceResult = StartOrderStore.StartPriceResult.Failed(msg.message)
            )

            is Message.GetPriceLoading -> copy(
                startPriceResult = StartOrderStore.StartPriceResult.Loading
            )

            is Message.GetPriceSuccess -> copy(
                startPriceResult = StartOrderStore.StartPriceResult.Success(msg.newPrice),
                startOrderInfo = startOrderInfo.copy(cost = msg.newPrice)
            )
        }
    }
}