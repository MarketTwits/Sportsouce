package com.markettwits.sportsouce.start.register.presentation.promo.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.sportsouce.start.register.presentation.promo.store.RegistrationPromoStore.Intent
import com.markettwits.sportsouce.start.register.presentation.promo.store.RegistrationPromoStore.Label
import com.markettwits.sportsouce.start.register.presentation.promo.store.RegistrationPromoStore.State

interface RegistrationPromoStore : Store<Intent, State, Label> {

    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val promo: String = "",
        val message: String = ""
    )

    sealed interface Intent {
        data class OnPromoChanged(val promo: String) : Intent
        data object OnClickPromo : Intent
        data object Dismiss : Intent
    }

    sealed interface Message {
        data class OnPromoChanged(val promo: String) : Message
        data object ApplyPromoSuccess : Message
        data class ApplyPromoFailed(val message: String) : Message
        data object ApplyPromoLoading : Message
    }

    sealed interface Label {
        data class ApplyPromo(val promo: String, val percent: Int) : Label
        data object Dismiss : Label
    }

}
