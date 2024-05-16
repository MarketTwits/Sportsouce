package com.markettwits.club.dashboard.presentation.dashboard.store

import androidx.compose.runtime.Immutable
import com.markettwits.club.dashboard.domain.Subscription
import com.markettwits.club.dashboard.domain.SubscriptionItems
import com.markettwits.club.info.domain.models.ClubInfo

@Immutable
data class SubscriptionUiState(
    val items: List<SubscriptionsUi>,
    val priceInfo: PriceInfoUi,
    val clubInfo: List<ClubInfo>
)

@Immutable
data class SubscriptionsUi(
    val isSelected: Boolean,
    val title: String,
    val subscriptions: List<SubscriptionUi>
)

@Immutable
data class SubscriptionUi(
    val isSelected: Boolean,
    val subscription: Subscription
)

@Immutable
data class PriceInfoUi(
    val monthOfCount: Int,
    val totalCost: Int,
)

internal fun SubscriptionUiState.getSelectedSubscriptionUi(): SubscriptionUi? =
    items.flatMap { it.subscriptions }
        .firstOrNull { it.isSelected }

internal fun List<SubscriptionItems>.mapSubscriptionsToUi(): List<SubscriptionsUi> =
    this.map {
        SubscriptionsUi(
            isSelected = false,
            title = it.groupName,
            subscriptions = it.subscription.mapSubscriptionToUi()
        )
    }

internal fun List<Subscription>.mapSubscriptionToUi(): List<SubscriptionUi> =
    this.map {
        SubscriptionUi(
            isSelected = false,
            subscription = it,
        )
    }