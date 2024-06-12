package com.markettwits.club.dashboard.presentation.dashboard.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.club.common.domain.ClubRepository
import com.markettwits.club.dashboard.presentation.dashboard.store.ClubDashboardStore.Intent
import com.markettwits.club.dashboard.presentation.dashboard.store.ClubDashboardStore.Label
import com.markettwits.club.dashboard.presentation.dashboard.store.ClubDashboardStore.Message
import com.markettwits.club.dashboard.presentation.dashboard.store.ClubDashboardStore.State
import com.markettwits.club.registration.domain.RegistrationType
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

internal class ClubDashboardExecutor(
    private val clubRepository: ClubRepository,
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.OnClickBack -> publish(Label.GoBack)
            is Intent.RetryRequest -> launchDashboard(getState().subscription) {
                launchClubInfo(it)
            }

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

            is Intent.OnClickInfo -> publish(
                Label.OnClickInfo(
                    intent.index,
                    getState().subscription.clubInfo
                )
            )
            is Intent.OnClickRegistration -> publish(Label.OnClickRegistration(intent.type))


            Intent.OnClickRegistrationSubscription -> onClickRegistrationSubscription(getState().subscription)
        }
    }

    override fun executeAction(action: Unit, getState: () -> State) {
        launchDashboard(getState().subscription) {
            launchClubInfo(it)
        }
    }

    private fun onClickRegistrationSubscription(state: SubscriptionUiState) {
        val id = (state.getSelectedSubscriptionUi()?.subscription?.id ?: 0)
        val count = state.priceInfo.monthOfCount
        publish(Label.OnClickRegistration(RegistrationType.Subscription(count = count, id = id)))
    }

    private fun launchDashboard(
        state: SubscriptionUiState,
        onCompletion: suspend (SubscriptionUiState) -> Unit
    ) {
        scope.launch {

            clubRepository.subscriptions()
                .onStart { dispatch(Message.Loading) }
                .catch { dispatch(Message.Failed(it.message.toString())) }
                .collect {
                    val newState = mapSubscriptionsInit(it, state)
                    dispatch(Message.Loaded(newState))
                    onCompletion(newState)
                }
        }
    }

    private suspend fun launchClubInfo(state: SubscriptionUiState) {
        clubRepository.clubInfo().onSuccess {
            dispatch(Message.Loaded(state.copy(clubInfo = it)))
        }.onFailure {
            println(it.message.toString())
        }
    }
}
