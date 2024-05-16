package com.markettwits.club.dashboard.presentation.dashboard.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.club.dashboard.domain.ClubRepository
import com.markettwits.club.dashboard.presentation.dashboard.store.ClubDashboardStore.Intent
import com.markettwits.club.dashboard.presentation.dashboard.store.ClubDashboardStore.Label
import com.markettwits.club.dashboard.presentation.dashboard.store.ClubDashboardStore.State
import com.markettwits.club.dashboard.presentation.dashboard.store.ClubDashboardStore.Message
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

internal class ClubDashboardExecutor(
    private val repository: ClubRepository
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.OnClickBack -> publish(Label.GoBack)
            is Intent.RetryRequest -> launch(getState().subscription)
            is Intent.OnClickKindOfSport -> dispatch(
                Message.UpdateState(
                    state = onClickKindOfSport(getState().subscription, intent.subscriptionsUi),
                )
            )

            is Intent.OnClickSubscription -> dispatch(
                Message.UpdateState(
                    onClickSubscription(getState().subscription, intent.subscriptionUi),
                )
            )

            is Intent.OnClickDecrease -> dispatch(
                Message.UpdateState(
                    onClickDecreaseMonth(getState().subscription)
                )
            )

            is Intent.OnClickIncrease -> dispatch(
                Message.UpdateState(
                    onClickIncreaseMonth(getState().subscription)
                )
            )

            is Intent.OnClickInfo -> publish(Label.OnClickInfo(intent.index))
        }
    }

    override fun executeAction(action: Unit, getState: () -> State) {
        launch(getState().subscription)
    }

    private fun launch(state: SubscriptionUiState) {
        scope.launch {
            repository.subscriptions()
                .onStart { dispatch(Message.Loading) }
                .catch { dispatch(Message.Failed(it.message.toString())) }
                .collect { dispatch(Message.Loaded(mapSubscriptionsInit(it, state))) }
        }
    }
}
