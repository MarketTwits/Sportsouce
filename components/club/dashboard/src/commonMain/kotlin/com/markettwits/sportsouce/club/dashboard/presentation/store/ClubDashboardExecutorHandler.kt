package com.markettwits.sportsouce.club.dashboard.presentation.store

import com.markettwits.sportsouce.club.dashboard.domain.SubscriptionItems


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


internal fun mapSubscriptionsInit(
    subscriptionsList: List<SubscriptionItems>,
    subscriptionUiState: SubscriptionUiState,
): SubscriptionUiState {
    val mapTop = initSubscriptions(subscriptionsList)
    val mapInner = initSubscription(mapTop)
    return subscriptionUiState.copy(items = mapInner)
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
    return newState
}

internal fun onClickSubscription(
    state: SubscriptionUiState,
    clickedSubscriptionUi: SubscriptionUi
): SubscriptionUiState {
    val subscriptions = state.items.map { subscriptionsUi ->
        if (subscriptionsUi.subscriptions.any { it.subscription.id == clickedSubscriptionUi.subscription.id }) {
            subscriptionsUi.copy(subscriptions = subscriptionsUi.subscriptions.map { subscriptionUi ->
                subscriptionUi.copy(
                    isSelected = subscriptionUi.subscription.id == clickedSubscriptionUi.subscription.id
                )
            })
        } else {
            subscriptionsUi
        }
    }
    return state.copy(items = subscriptions)
}

internal fun updateSubscriptionsList(
    state: SubscriptionUiState,
    item : SubscriptionUi
) : SubscriptionUiState {
    val updatedItems = state.items.map { subscriptionsUi ->
        subscriptionsUi.copy(
            subscriptions = subscriptionsUi.subscriptions.map { subscriptionUi ->
                if (subscriptionUi.subscription.id == item.subscription.id) {
                    subscriptionUi.copy(
                        isSelected = item.isSelected,
                        subscription = item.subscription,
                        isDependsOnCount = item.isDependsOnCount,
                        monthOfCount = item.monthOfCount
                    )
                } else {
                    subscriptionUi
                }
            }
        )
    }
    return state.copy(items = updatedItems)
}