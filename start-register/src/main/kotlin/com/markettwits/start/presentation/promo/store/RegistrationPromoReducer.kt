package com.markettwits.start.presentation.promo.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.start.presentation.promo.store.RegistrationPromoStore.Message
import com.markettwits.start.presentation.promo.store.RegistrationPromoStore.State

object RegistrationPromoReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.ApplyPromoFailed -> copy(
                isLoading = false,
                isError = true,
                message = msg.message
            )

            is Message.ApplyPromoLoading -> copy(isLoading = true)
            is Message.ApplyPromoSuccess -> copy()
            is Message.OnPromoChanged -> copy(promo = msg.promo)
        }
    }
}