package com.markettwits.sportsouce.club.subscription.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.sportsouce.club.common.domain.ClubRepository
import com.markettwits.sportsouce.club.registration.domain.RegistrationType
import com.markettwits.sportsouce.club.registration.domain.WorkoutPriceForm
import kotlinx.coroutines.launch

class SubscriptionPricingStoreFactory(
    private val storeFactory: StoreFactory,
    private val clubRepository: ClubRepository,
) {

    fun create(): SubscriptionPricingStore {
        return object : SubscriptionPricingStore,
            Store<SubscriptionPricingStore.Intent, SubscriptionPricingStore.State, SubscriptionPricingStore.Label> by storeFactory.create(
                name = "SubscriptionPricingStore",
                initialState = SubscriptionPricingStore.State(),
                bootstrapper = BootstrapperImpl(),
                executorFactory = { ExecutorImpl(clubRepository) },
                reducer = ReducerImpl
            ) {}
    }

    private sealed interface Action {
        data object LoadSubscriptions : Action
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            dispatch(Action.LoadSubscriptions)
        }
    }

    private class ExecutorImpl(
        private val clubRepository: ClubRepository,
    ) : CoroutineExecutor<SubscriptionPricingStore.Intent, Action, SubscriptionPricingStore.State, SubscriptionPricingStore.Message, SubscriptionPricingStore.Label>() {
        override fun executeAction(action: Action) {
            when (action) {
                is Action.LoadSubscriptions -> {
                    scope.launch {
                        dispatch(SubscriptionPricingStore.Message.Loading)
                        try {
                            clubRepository.subscriptions().collect { subscriptionsList ->
                                dispatch(SubscriptionPricingStore.Message.Loaded(subscriptionsList))
                            }
                        } catch (e: Exception) {
                            dispatch(SubscriptionPricingStore.Message.Failed(SauceError.Empty(e)))
                        }
                    }
                }
            }
        }

        override fun executeIntent(intent: SubscriptionPricingStore.Intent) {
            when (intent) {
                is SubscriptionPricingStore.Intent.OnClickBack -> {
                    publish(SubscriptionPricingStore.Label.Dismiss)
                }

                is SubscriptionPricingStore.Intent.RetryRequest -> {
                    executeAction(Action.LoadSubscriptions)
                }

                is SubscriptionPricingStore.Intent.UpdateMonthCount -> {
                    scope.launch {
                        dispatch(SubscriptionPricingStore.Message.PriceCalculationStarted(intent.subscriptionId))
                        try {
                            val workoutPrice = clubRepository.workoutRegistrationPrice(
                                WorkoutPriceForm(
                                    type = "subscription",
                                    count = intent.monthCount,
                                    id = intent.subscriptionId
                                )
                            ).getOrThrow()

                            dispatch(
                                SubscriptionPricingStore.Message.PriceCalculationSuccess(
                                    subscriptionId = intent.subscriptionId,
                                    workoutPrice = workoutPrice,
                                    monthCount = intent.monthCount
                                )
                            )
                        } catch (e: Exception) {
                            dispatch(
                                SubscriptionPricingStore.Message.PriceCalculationFailed(
                                    subscriptionId = intent.subscriptionId,
                                    error = SauceError.Empty(e)
                                )
                            )
                        }
                    }
                }

                is SubscriptionPricingStore.Intent.OnClickSubscribe -> {
                    publish(
                        SubscriptionPricingStore.Label.Registration(
                            RegistrationType.Subscription(
                                state().priceCalculations[intent.subscriptionId.toString()]?.monthCount ?: 1,
                                intent.subscriptionId,
                                intent.subscriptionName
                            )
                        )
                    )
                }
            }
        }

    }

    private object ReducerImpl : Reducer<SubscriptionPricingStore.State, SubscriptionPricingStore.Message> {
        override fun SubscriptionPricingStore.State.reduce(msg: SubscriptionPricingStore.Message): SubscriptionPricingStore.State =
            when (msg) {
                is SubscriptionPricingStore.Message.Loading -> copy(
                    isLoading = true,
                    error = null
                )

                is SubscriptionPricingStore.Message.Failed -> copy(
                    isLoading = false,
                    error = msg.error
                )

                is SubscriptionPricingStore.Message.Loaded -> copy(
                    isLoading = false,
                    error = null,
                    subscriptions = msg.subscriptions
                )

                is SubscriptionPricingStore.Message.PriceCalculationStarted -> {
                    val currentCalculation = priceCalculations[msg.subscriptionId.toString()]
                        ?: SubscriptionPricingStore.PriceCalculationState()
                    copy(
                        priceCalculations = priceCalculations + (msg.subscriptionId.toString() to currentCalculation.copy(
                            isLoading = true,
                            error = null
                        ))
                    )
                }

                is SubscriptionPricingStore.Message.PriceCalculationSuccess -> {
                    copy(
                        priceCalculations = priceCalculations + (msg.subscriptionId.toString() to SubscriptionPricingStore.PriceCalculationState(
                            isLoading = false,
                            workoutPrice = msg.workoutPrice,
                            error = null,
                            monthCount = msg.monthCount
                        ))
                    )
                }

                is SubscriptionPricingStore.Message.PriceCalculationFailed -> {
                    val currentCalculation = priceCalculations[msg.subscriptionId.toString()]
                        ?: SubscriptionPricingStore.PriceCalculationState()
                    copy(
                        priceCalculations = priceCalculations + (msg.subscriptionId.toString() to currentCalculation.copy(
                            isLoading = false,
                            error = msg.error
                        ))
                    )
                }
            }
    }
}