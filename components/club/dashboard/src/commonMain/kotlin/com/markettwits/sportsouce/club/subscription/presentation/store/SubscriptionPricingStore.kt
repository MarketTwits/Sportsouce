package com.markettwits.sportsouce.club.subscription.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.sportsouce.club.dashboard.domain.SubscriptionItems
import com.markettwits.sportsouce.club.registration.domain.WorkoutPrice

interface SubscriptionPricingStore :
    Store<SubscriptionPricingStore.Intent, SubscriptionPricingStore.State, SubscriptionPricingStore.Label> {

    data class State(
        val subscriptions: List<SubscriptionItems> = emptyList(),
        val isLoading: Boolean = false,
        val error: SauceError? = null,
        val priceCalculations: Map<String, PriceCalculationState> = emptyMap(),
    )

    data class PriceCalculationState(
        val isLoading: Boolean = false,
        val workoutPrice: WorkoutPrice? = null,
        val error: SauceError? = null,
        val monthCount: Int = 1,
    )

    sealed interface Intent {
        data object OnClickBack : Intent
        data object RetryRequest : Intent
        data class UpdateMonthCount(val subscriptionId: Int, val monthCount: Int) : Intent
        data class OnClickSubscribe(val subscriptionId: Int) : Intent
    }

    sealed interface Message {
        data object Loading : Message
        data class Failed(val error: SauceError) : Message
        data class Loaded(val subscriptions: List<SubscriptionItems>) : Message
        data class PriceCalculationStarted(val subscriptionId: Int) : Message
        data class PriceCalculationSuccess(
            val subscriptionId: Int,
            val workoutPrice: WorkoutPrice,
            val monthCount: Int,
        ) : Message

        data class PriceCalculationFailed(val subscriptionId: Int, val error: SauceError) : Message
    }

    sealed interface Label {
        data object Dismiss : Label
        data class Registration(val type: com.markettwits.sportsouce.club.registration.domain.RegistrationType) : Label
    }
}