package com.markettwits.sportsouce.start.register.presentation.promo.component

import com.markettwits.sportsouce.start.register.presentation.promo.store.RegistrationPromoStore
import kotlinx.coroutines.flow.StateFlow

interface RegistrationPromoComponent {

    val state: StateFlow<RegistrationPromoStore.State>

    fun obtainEvent(intent: RegistrationPromoStore.Intent)
}
