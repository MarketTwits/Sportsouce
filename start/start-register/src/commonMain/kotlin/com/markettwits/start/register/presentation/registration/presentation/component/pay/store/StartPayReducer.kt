package com.markettwits.start.register.presentation.registration.presentation.component.pay.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.start.register.presentation.registration.presentation.component.pay.store.StartPayStore.State
import com.markettwits.start.register.presentation.registration.presentation.component.pay.store.StartPayStore.Message

object StartPayReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {

            is Message.UpdatePromoSuccess -> copy(
                isLoading = false,
                state = state.copy(
                    startInfo = state.startInfo.copy(promo = msg.promo)
                )
            )

            is Message.UpdatePrice -> copy(
                isLoading = false,
                state = state.copy(price = msg.price)
            )

            is Message.UpdatePriceLoadingFinished -> copy(isLoading = false)

            is Message.UpdatePriceLoadingStarted -> copy(isLoading = true)
        }
    }
}