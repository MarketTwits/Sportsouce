package com.markettwits.registrations.start_order_profile.store.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.registrations.registrations.domain.StartOrderInfo
import com.markettwits.registrations.start_order_profile.store.store.StartOrderStore.Intent
import com.markettwits.registrations.start_order_profile.store.store.StartOrderStore.Label
import com.markettwits.registrations.start_order_profile.store.store.StartOrderStore.State

interface StartOrderStore : Store<Intent, State, Label> {
    data class State(
        val isLoading: Boolean = false,
        val isFailed: Boolean = false,
        val message: String = "",
        val paymentUrl: String = "",
        val startOrderInfo: StartOrderInfo
    )

    sealed interface Intent {
        data object Dismiss : Intent
        data class OnClickPay(val orderId: Int) : Intent
    }

    sealed interface Message {
        data object Loading : Message
        data class Success(val paymentUrl: String) : Message
        data class Failed(val message: String) : Message
    }

    sealed interface Label {
        data object Dismiss : Label
    }

}
