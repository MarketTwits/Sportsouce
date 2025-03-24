package com.markettwits.club.dashboard.presentation.store

import androidx.compose.runtime.Immutable
import com.markettwits.club.dashboard.domain.Subscription
import com.markettwits.club.dashboard.domain.SubscriptionItems
import com.markettwits.club.info.domain.models.ClubInfo

@Immutable
data class SubscriptionUiState(
    val items: List<SubscriptionsUi>,
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
    val isDependsOnCount: Boolean,
    val monthOfCount: Int,
    val subscription: Subscription
)


internal fun SubscriptionUiState.getSelectedSubscriptionUi(): SubscriptionUi =
    items.flatMap { it.subscriptions }
        .firstOrNull { it.isSelected } ?: throw IllegalStateException("#SubscriptionUiState don't selected")

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
            subscription = it,
            isSelected = false,
            monthOfCount = 1,
            isDependsOnCount = it.priceDependsOnCount
        )
    }