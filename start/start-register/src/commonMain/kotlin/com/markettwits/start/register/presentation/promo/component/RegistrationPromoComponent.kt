package com.markettwits.start.register.presentation.promo.component

import com.markettwits.start.register.presentation.promo.store.RegistrationPromoStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface RegistrationPromoComponent {

    val state: StateFlow<RegistrationPromoStore.State>

    val labels: Flow<RegistrationPromoStore.Label>

    fun obtainEvent(intent: RegistrationPromoStore.Intent)
}
