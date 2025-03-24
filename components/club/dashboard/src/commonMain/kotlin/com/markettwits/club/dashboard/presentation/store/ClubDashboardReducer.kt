package com.markettwits.club.dashboard.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.club.dashboard.presentation.store.ClubDashboardStore.State
import com.markettwits.club.dashboard.presentation.store.ClubDashboardStore.Message

internal object ClubDashboardReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.Failed -> copy(isLoading = false, error = msg.error)
            is Message.Loaded -> copy(isLoading = false, error = null, subscription = msg.items)
            is Message.Loading -> copy(isLoading = true, error = null)
            is Message.UpdateState -> copy(subscription = msg.state)
            is Message.UpdateSubscriptionPanelState -> copy(subscriptionPanelState = msg.state)
        }
    }
}