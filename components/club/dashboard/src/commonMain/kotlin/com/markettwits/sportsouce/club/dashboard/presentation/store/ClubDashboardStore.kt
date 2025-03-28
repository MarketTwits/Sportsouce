package com.markettwits.sportsouce.club.dashboard.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.sportsouce.club.dashboard.presentation.store.ClubDashboardStore.Intent
import com.markettwits.sportsouce.club.dashboard.presentation.store.ClubDashboardStore.Label
import com.markettwits.sportsouce.club.dashboard.presentation.store.ClubDashboardStore.State
import com.markettwits.sportsouce.club.info.domain.models.ClubInfo
import com.markettwits.sportsouce.club.registration.domain.RegistrationType

interface ClubDashboardStore : Store<Intent, State, Label> {

    data class State(
        val isLoading: Boolean = false,
        val error: SauceError? = null,
        val subscription: SubscriptionUiState = SubscriptionUiState(
            items = emptyList(),
            clubInfo = emptyList()
        ),
        val subscriptionPanelState : SubscriptionPanelState = SubscriptionPanelState(
            isLoading = false,
            isShowCounter = false,
            isIncreaseEnable = true,
            isDecreaseEnable = true
        ),
    )

    sealed interface Intent {
        data object OnClickBack : Intent
        data class OnClickRegistration(val type: RegistrationType) : Intent
        data object OnClickRegistrationSubscription : Intent
        data class OnClickKindOfSport(val subscriptionsUi: SubscriptionsUi) : Intent
        data class OnClickSubscriptionItem(val subscriptionUi: SubscriptionUi) : Intent
        data class OnClickInfo(val index: Int) : Intent
        data object OnClickIncrease : Intent
        data object OnClickDecrease : Intent
        data object RetryRequest : Intent
    }

    sealed interface Message {
        data object Loading : Message
        data class Failed(val error: SauceError) : Message
        data class Loaded(val items: SubscriptionUiState) : Message
        data class UpdateSubscriptionPanelState(val state : SubscriptionPanelState) : Message
        data class UpdateState(val state: SubscriptionUiState) : Message
    }

    sealed interface Label {
        data object GoBack : Label
        data class OnClickInfo(val index: Int, val items: List<ClubInfo>) : Label
        data class OnClickRegistration(val type: RegistrationType) : Label
    }

}
