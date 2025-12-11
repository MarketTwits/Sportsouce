package com.markettwits.sportsouce.profile.registrations.presentation.detail.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.presentation.detail.store.StartOrderStore.*

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
        data object OnClickUpdatePrice : Intent
        data object OnClickHelp : Intent
        data class OnClickReRegistration(val startId: Int, val orderId: Int) : Intent
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
        data class OnClickReRegistration(val startId: Int, val orderId: Int) : Label
    }

}
