package com.markettwits.club.dashboard.presentation.dashboard.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.bottom_bar.component.listener.BottomBarVisibilityListener
import com.markettwits.club.dashboard.presentation.dashboard.store.ClubDashboardStore.Intent
import com.markettwits.club.dashboard.presentation.dashboard.store.ClubDashboardStore.Label
import com.markettwits.club.dashboard.presentation.dashboard.store.ClubDashboardStore.State
import com.markettwits.club.info.domain.models.ClubInfo
import com.markettwits.club.registration.domain.RegistrationType

interface ClubDashboardStore : Store<Intent, State, Label> {
    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val message: String = "",
        val subscription: SubscriptionUiState = SubscriptionUiState(
            items = emptyList(),
            priceInfo = PriceInfoUi(1, 0),
            clubInfo = emptyList()
        ),
        val bottomBarVisibilityListener: BottomBarVisibilityListener
    )

    sealed interface Intent {
        data object OnClickBack : Intent
        data class OnClickRegistration(val type: RegistrationType) : Intent
        data object OnClickRegistrationSubscription : Intent
        data class OnClickKindOfSport(val subscriptionsUi: SubscriptionsUi) : Intent
        data class OnClickSubscription(val subscriptionUi: SubscriptionUi) : Intent
        data class OnClickInfo(val index: Int) : Intent
        data object OnClickIncrease : Intent
        data object OnClickDecrease : Intent
        data object RetryRequest : Intent
    }

    sealed interface Message {
        data object Loading : Message
        data class Failed(val message: String) : Message
        data class Loaded(val items: SubscriptionUiState) : Message
        data class UpdateState(val state: SubscriptionUiState) : Message
    }

    sealed interface Label {
        data object GoBack : Label
        data class OnClickInfo(val index: Int, val items: List<ClubInfo>) : Label
        data class OnClickRegistration(val type: RegistrationType) : Label
    }

}
