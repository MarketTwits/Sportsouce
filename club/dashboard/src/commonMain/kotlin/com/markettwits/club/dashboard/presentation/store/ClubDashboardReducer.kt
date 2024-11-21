package com.markettwits.club.dashboard.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.club.dashboard.presentation.store.ClubDashboardStore.State
import com.markettwits.club.dashboard.presentation.store.ClubDashboardStore.Message

internal object ClubDashboardReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.Failed -> copy(isLoading = false, isError = true, message = msg.message)
            is Message.Loaded -> copy(isLoading = false, isError = false, subscription = msg.items)
            is Message.Loading -> copy(isLoading = true, isError = false)
            is Message.UpdateState -> copy(subscription = msg.state)
            is Message.UpdateSubscriptionPanelState -> copy(subscriptionPanelState = msg.state)
        }
    }
}