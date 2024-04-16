package com.markettwits.registrations.detail.store.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.registrations.detail.store.store.StartOrderStore.Intent
import com.markettwits.registrations.detail.store.store.StartOrderStore.Label
import com.markettwits.registrations.detail.store.store.StartOrderStore.State
import com.markettwits.registrations.list.domain.StartOrderInfo

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
        data class OnClickStart(val startId: Int) : Intent
    }

    sealed interface Message {
        data object Loading : Message
        data class Success(val paymentUrl: String) : Message
        data class Failed(val message: String) : Message
    }

    sealed interface Label {
        data object Dismiss : Label
        data class OnClickStart(val startId: Int) : Label
    }

}
