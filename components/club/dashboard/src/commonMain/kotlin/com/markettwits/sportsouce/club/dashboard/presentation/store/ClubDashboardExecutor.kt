package com.markettwits.sportsouce.club.dashboard.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core.errors.api.throwable.isNetworkConnectionError
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.sportsouce.club.common.domain.ClubRepository
import com.markettwits.sportsouce.club.dashboard.presentation.store.ClubDashboardStore.Intent
import com.markettwits.sportsouce.club.dashboard.presentation.store.ClubDashboardStore.Label
import com.markettwits.sportsouce.club.dashboard.presentation.store.ClubDashboardStore.Message
import com.markettwits.sportsouce.club.dashboard.presentation.store.ClubDashboardStore.State
import com.markettwits.sportsouce.club.registration.domain.RegistrationType
import com.markettwits.sportsouce.club.registration.domain.WorkoutPriceForm
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

internal class ClubDashboardExecutor(
    private val clubRepository: ClubRepository,
    private val exceptionTracker: ExceptionTracker
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.OnClickBack -> publish(Label.GoBack)

            is Intent.RetryRequest -> launchDashboard(state()) {
                launchClubInfo(it)
            }

            is Intent.OnClickKindOfSport -> {
                val newState = onClickKindOfSport(state().subscription, intent.subscriptionsUi)
                dispatch(Message.UpdateState(newState))
                dispatch(Message.UpdateSubscriptionPanelState(
                    subscriptionStateUpdated(newState.getSelectedSubscriptionUi())
                ))
            }

            is Intent.OnClickSubscriptionItem -> {
                val newItem = onClickSubscription(state().subscription, intent.subscriptionUi)
                dispatch(Message.UpdateState(newItem))
                dispatch(Message.UpdateSubscriptionPanelState(
                    subscriptionStateUpdated(newItem.getSelectedSubscriptionUi())
                ))
            }

            is Intent.OnClickDecrease -> {
                launchUpdatePrice(state(), false)
            }

            is Intent.OnClickIncrease -> {
                launchUpdatePrice(state(), true)
            }

            is Intent.OnClickInfo -> publish(
                Label.OnClickInfo(
                    index = intent.index,
                    items = state().subscription.clubInfo
                )
            )

            is Intent.OnClickRegistration -> publish(Label.OnClickRegistration(intent.type))

            is Intent.OnClickRegistrationSubscription -> onClickRegistrationSubscription(state().subscription)
        }
    }

    override fun executeAction(action: Unit) {
        launchDashboard(state()) {
            launchClubInfo(it)
        }
    }

    private fun onClickRegistrationSubscription(state: SubscriptionUiState) {
        val item = (state.getSelectedSubscriptionUi())
        publish(
            Label.OnClickRegistration(
                RegistrationType.Subscription(
                    count = item.monthOfCount, id = item.subscription.id
                )
            )
        )
    }

    private fun launchDashboard(
        state: State,
        onCompletion: suspend (SubscriptionUiState) -> Unit
    ) {
        scope.launch {
            clubRepository.subscriptions()
                .onStart { dispatch(Message.Loading) }
                .catch { dispatch(Message.Failed(it.mapToSauceError()))}
                .collect {
                    val newState = mapSubscriptionsInit(it, state.subscription)
                    dispatch(Message.Loaded(newState))
                    dispatch(
                        Message.UpdateSubscriptionPanelState(
                            subscriptionStateUpdated(
                                newState.getSelectedSubscriptionUi()
                            )
                        )
                    )
                    onCompletion(newState)
                }
        }
    }

    private suspend fun launchClubInfo(state: SubscriptionUiState) {
        clubRepository.clubInfo().onSuccess {
            dispatch(Message.Loaded(state.copy(clubInfo = it)))
        }.onFailure {
            if (!it.isNetworkConnectionError())
                exceptionTracker.reportException(
                    exception = it,
                    key = "ClubDashboardExecutor#launchClubInfo"
                )
        }
    }

    private fun launchUpdatePrice(
        state: ClubDashboardStore.State,
        isIncrease: Boolean
    ) {
        scope.launch {
            val item = state.subscription.getSelectedSubscriptionUi()
            val count = if (isIncrease) item.monthOfCount + 1 else item.monthOfCount - 1
            dispatch(
                Message.UpdateSubscriptionPanelState(
                    state.subscriptionPanelState.subscriptionStateLoading()
                )
            )
            clubRepository.workoutRegistrationPrice(
                WorkoutPriceForm(
                    type = "subscription",
                    count = count,
                    id = item.subscription.id
                )
            ).fold(onSuccess = {
                val itemWithNewPrice = item.copy(
                    subscription = item.subscription.copy(price = it.totalPrice),
                    monthOfCount = count
                )
                val newState = updateSubscriptionsList(state.subscription, itemWithNewPrice)
                dispatch(
                    Message.UpdateState(
                        newState
                    )
                )
                dispatch(
                    Message.UpdateSubscriptionPanelState(
                        subscriptionStateUpdated(newState.getSelectedSubscriptionUi())
                    )
                )
            }, onFailure = {
                dispatch(
                    Message.UpdateSubscriptionPanelState(
                        subscriptionStateUpdated(item)
                    )
                )
            })
        }
    }
}
