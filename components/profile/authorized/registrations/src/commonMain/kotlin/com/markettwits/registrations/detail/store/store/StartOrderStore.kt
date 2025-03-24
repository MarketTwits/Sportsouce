package com.markettwits.registrations.detail.store.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.registrations.detail.store.store.StartOrderStore.Intent
import com.markettwits.registrations.detail.store.store.StartOrderStore.Label
import com.markettwits.registrations.detail.store.store.StartOrderStore.State
import com.markettwits.registrations.list.domain.StartOrderInfo

interface StartOrderStore : Store<Intent, State, Label> {

    data class State(
        val startPriceResult: StartPriceResult = StartPriceResult.Loading,
        val startOrderInfo: StartOrderInfo
    )

    sealed interface StartPriceResult {
        data object Loading : StartPriceResult
        data object Free : StartPriceResult
        data class Failed(val message: String) : StartPriceResult
        data class Success(val price: String) : StartPriceResult
    }

    sealed interface Intent {
        data object Dismiss : Intent
        data class OnClickPay(val orderId: Int) : Intent
        data class OnClickStart(val startId: Int) : Intent
    }

    sealed interface Message {
        data object GetPriceLoading : Message
        data class GetPriceSuccess(val newPrice: String) : Message
        data class GetPriceFailed(val message: String) : Message
        data object GetPriceDontRequired : Message
    }

    sealed interface Label {
        data object Dismiss : Label
        data class OnClickStart(val startId: Int) : Label
    }

}
