package com.markettwits.sportsouce.club.dashboard.presentation.store

import androidx.compose.runtime.Immutable

@Immutable
data class SubscriptionPanelState(
    val isLoading : Boolean,
    val isShowCounter : Boolean,
    val isIncreaseEnable : Boolean,
    val isDecreaseEnable : Boolean
)

internal fun subscriptionStateUpdated(
    selectedSubscriptionUi: SubscriptionUi
) : SubscriptionPanelState {
    val isIncreaseEnable = selectedSubscriptionUi.subscription.maxAmount > selectedSubscriptionUi.monthOfCount
    val isDecreaseEnable = selectedSubscriptionUi.monthOfCount > 1
    return SubscriptionPanelState(
        isLoading = false,
        isIncreaseEnable = isIncreaseEnable,
        isDecreaseEnable = isDecreaseEnable,
        isShowCounter = selectedSubscriptionUi.isDependsOnCount
    )
}

internal fun SubscriptionPanelState.subscriptionStateLoading() : SubscriptionPanelState =
    copy(
        isLoading = true,
        isIncreaseEnable = false,
        isDecreaseEnable = false
    )
