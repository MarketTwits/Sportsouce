package com.markettwits.start.register.presentation.registration.pay.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.start.register.presentation.registration.common.domain.models.StartRegistrationPriceResult
import com.markettwits.start.register.presentation.registration.pay.store.StartPayStore.Intent
import com.markettwits.start.register.presentation.registration.pay.store.StartPayStore.Label
import com.markettwits.start.register.presentation.registration.pay.store.StartPayStore.State
import com.markettwits.start.register.presentation.registration.registration.components.StartRegistrationStagePage

interface StartPayStore : Store<Intent, State, Label> {

    data class State(
        val isLoading : Boolean,
        val state : StartRegistrationStagePage.Pay
    )

    sealed interface Intent{

        data object OnClickPromo : Intent

        data object OnClickGoBack : Intent

        data object OnClickRegistryWithPay : Intent

        data object OnClickRegistryWithoutPay : Intent

        data class UpdatePromo(val promo : String) : Intent

    }

    sealed interface Message{

        data class UpdatePrice(val price : StartRegistrationPriceResult) : Message

        data class UpdatePromoSuccess(val promo : String) : Message

        data object UpdatePriceLoadingStarted : Message

        data object UpdatePriceLoadingFinished : Message

    }


    sealed interface Label{

        data object GoBack : Label

        data class SendEvent(val eventContent: EventContent) : Label

        data class GoPromo(val value : String) : Label

        data object GoSuccess : Label

    }

}
