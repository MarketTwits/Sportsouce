package com.markettwits.club.dashboard.presentation.dashboard.store

import com.markettwits.club.dashboard.domain.SubscriptionItems


private fun initSubscriptions(subscriptionsList: List<SubscriptionItems>): List<SubscriptionsUi> {
    val subscriptionsListUi = subscriptionsList.mapSubscriptionsToUi()
    if (subscriptionsListUi.isEmpty()) return subscriptionsListUi
    val firstItem = subscriptionsListUi.first().copy(isSelected = true)
    val restItems = subscriptionsListUi.drop(1).map { it.copy(isSelected = false) }
    return listOf(firstItem) + restItems
}

private fun initSubscription(subscriptionsList: List<SubscriptionsUi>): List<SubscriptionsUi> {
    return subscriptionsList.map { subscriptionsUi ->
        val highestPriceSubscriptionUi =
            subscriptionsUi.subscriptions.maxByOrNull { it.subscription.price }
        val updatedSubscriptions = subscriptionsUi.subscriptions.map { subscriptionUi ->
            if (subscriptionUi.subscription.id == highestPriceSubscriptionUi?.subscription?.id) {
                subscriptionUi.copy(isSelected = true)
            } else {
                subscriptionUi.copy(isSelected = false)
            }
        }
        subscriptionsUi.copy(subscriptions = updatedSubscriptions)
    }
}

private fun mapPriceInfo(newSubscriptionUiState: SubscriptionUiState): PriceInfoUi {
    newSubscriptionUiState.items.forEach { subscriptionsUi ->
        if (subscriptionsUi.isSelected) {
            subscriptionsUi.subscriptions.forEach { subscriptionUi ->
                if (subscriptionUi.isSelected) {
                    val month = newSubscriptionUiState.priceInfo.monthOfCount
                    if (month < 1 || month > 12) return newSubscriptionUiState.priceInfo
                    val discount =
                        subscriptionUi.subscription.discount + (5 * (month - 1)).coerceAtMost(50)
                    val finalPrice =
                        subscriptionUi.subscription.price * month * (1 - discount / 100.0)
                    val totalCost = finalPrice.toInt()
                    return PriceInfoUi(newSubscriptionUiState.priceInfo.monthOfCount, totalCost)
                }
            }
        }
    }
    return throw RuntimeException()
}

internal fun mapSubscriptionsInit(
    subscriptionsList: List<SubscriptionItems>,
    subscriptionUiState: SubscriptionUiState,
): SubscriptionUiState {
    val mapTop = initSubscriptions(subscriptionsList)
    val mapInner = initSubscription(mapTop)
    val newState = subscriptionUiState.copy(items = mapInner)
    return newState.copy(priceInfo = mapPriceInfo(newState))
}

internal fun onClickKindOfSport(
    state: SubscriptionUiState,
    subscriptionsUi: SubscriptionsUi
): SubscriptionUiState {
    val newSubscriptions = state.items.map { subscriptionUi ->
        if (subscriptionUi.title == subscriptionsUi.title) {
            subscriptionUi.copy(isSelected = true)
        } else {
            subscriptionUi.copy(isSelected = false)
        }
    }
    val newState = state.copy(
        items = newSubscriptions,
    )
    return newState.copy(priceInfo = mapPriceInfo(newState))
}

internal fun onClickSubscription(
    state: SubscriptionUiState,
    clickedSubscriptionUi: SubscriptionUi
): SubscriptionUiState {
    val subscriptions = state.items.map { subscriptionsUi ->
        if (subscriptionsUi.subscriptions.any { it.subscription.id == clickedSubscriptionUi.subscription.id }) {
            subscriptionsUi.copy(subscriptions = subscriptionsUi.subscriptions.map { subscriptionUi ->
                subscriptionUi.copy(isSelected = subscriptionUi.subscription.id == clickedSubscriptionUi.subscription.id)
            })
        } else {
            subscriptionsUi
        }
    }
    val newState = state.copy(items = subscriptions)
    return newState.copy(priceInfo = mapPriceInfo(newState))
}

internal fun onClickIncreaseMonth(
    state: SubscriptionUiState,
): SubscriptionUiState {
    val newMonthCount = state.priceInfo.monthOfCount + 1
    if (newMonthCount > 12) return state
    val newPriceInfo = state.priceInfo.copy(monthOfCount = newMonthCount)
    return state.copy(priceInfo = mapPriceInfo(state.copy(priceInfo = newPriceInfo)))
}

internal fun onClickDecreaseMonth(
    state: SubscriptionUiState,
): SubscriptionUiState {
    val newMonthCount = state.priceInfo.monthOfCount - 1
    if (newMonthCount < 1) return state
    val newPriceInfo = state.priceInfo.copy(monthOfCount = newMonthCount)
    return state.copy(priceInfo = mapPriceInfo(state.copy(priceInfo = newPriceInfo)))
}
