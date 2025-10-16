package com.markettwits.sportsouce.club.subscription.component

import com.markettwits.sportsouce.club.registration.domain.RegistrationType
import com.markettwits.sportsouce.club.subscription.store.SubscriptionPricingStore
import kotlinx.coroutines.flow.StateFlow

interface SubscriptionPricingComponent {
    val state: StateFlow<SubscriptionPricingStore.State>
    fun obtainEvent(intent: SubscriptionPricingStore.Intent)

    sealed interface Output {
        data object Dismiss : Output
        data class Registration(val type: RegistrationType) : Output
    }
}