package com.markettwits.start.presentation.registration.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.cloud.ext_model.DistanceInfo
import com.markettwits.core_ui.event.consumed
import com.markettwits.core_ui.event.triggered
import com.markettwits.start.data.registration.RegistrationStartRepository
import com.markettwits.start.domain.StartStatement
import com.markettwits.start.presentation.registration.store.StartRegistrationStore.State
import kotlinx.coroutines.launch


class StartRegistrationStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: RegistrationStartRepository
) {

    fun create(price: String, distanceInfo: DistanceInfo, starId: Int): StartRegistrationStore =
        object : StartRegistrationStore,
            Store<StartRegistrationStore.Intent, StartRegistrationStore.State, StartRegistrationStore.Label> by storeFactory.create(
                name = "RegistrationStore",
                initialState = StartRegistrationStore.State(),
                bootstrapper = BootstrapperImpl(price),
                executorFactory = { ExecutorImpl(distanceInfo, starId) },
                reducer = ReducerImpl
            ) {}

    private sealed interface Action {
        data object Loading : Action
        data class InfoLoaded(val startStatement: StartStatement) : Action
        data class InfoFailed(val message: String) : Action
    }

    private inner class BootstrapperImpl(
        private val price: String,
    ) :
        CoroutineBootstrapper<Action>() {
        override fun invoke() {
            launch(price)
        }

        private fun launch(price: String) {
            dispatch(Action.Loading)
            scope.launch {
                repository.preload(price).fold(
                    onSuccess = { dispatch(Action.InfoLoaded(it)) },
                    onFailure = { dispatch(Action.InfoFailed(it.message.toString())) }
                )
            }
        }
    }

    private sealed interface Msg {
        data object Loading : Msg
        data class InfoLoaded(val startStatement: StartStatement) : Msg
        data class InfoFailed(val message: String) : Msg
        data class OnValueChanged(val startStatement: StartStatement) : Msg
        data class RegistryFailed(val message: String) : Msg
        data class RegistrySuccess(val paymentUrl: String, val message: String) : Msg
        data object RegistryLoading : Msg
        data object OnConsumedSucceededEvent : Msg
        data object OnConsumedFailedEvent : Msg
    }

    private inner class ExecutorImpl(
        private val distanceInfo: DistanceInfo,
        private val starId: Int
    ) :
        CoroutineExecutor<StartRegistrationStore.Intent, Action, StartRegistrationStore.State, Msg, StartRegistrationStore.Label>() {
        override fun executeIntent(intent: StartRegistrationStore.Intent, getState: () -> State) {
            when (intent) {
                is StartRegistrationStore.Intent.ChangeFiled -> dispatch(Msg.OnValueChanged(intent.startStatement))
                is StartRegistrationStore.Intent.GoBack -> publish(StartRegistrationStore.Label.GoBack)
                is StartRegistrationStore.Intent.OnClickPay -> save(
                    statement = getState().startStatement!!,
                    distanceInfo = distanceInfo,
                    starId = starId,
                    withPayment = true
                )

                is StartRegistrationStore.Intent.OnClickSave -> save(
                    statement = getState().startStatement!!,
                    distanceInfo = distanceInfo,
                    starId = starId,
                    withPayment = false
                )

                is StartRegistrationStore.Intent.OnConsumedFailedEvent -> dispatch(Msg.OnConsumedFailedEvent)
                is StartRegistrationStore.Intent.OnConsumedSucceededEvent -> dispatch(Msg.OnConsumedSucceededEvent)
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.InfoFailed -> dispatch(Msg.InfoFailed(action.message))
                is Action.InfoLoaded -> dispatch(Msg.InfoLoaded(action.startStatement))
                is Action.Loading -> dispatch(Msg.Loading)
            }
        }


        private fun save(
            withPayment: Boolean,
            statement: StartStatement,
            distanceInfo: DistanceInfo,
            starId: Int
        ) {
            scope.launch {
                dispatch(Msg.RegistryLoading)
                val result =
                    if (withPayment)
                        repository.pay(
                            statement = statement,
                            distanceInfo = distanceInfo,
                            startId = starId
                        )
                    else
                        repository.save(
                            statement = statement,
                            distanceInfo = distanceInfo,
                            startId = starId
                        )
                if (result.isError) {
                    dispatch(Msg.RegistryFailed(result.message))
                }
                if (result.isSuccess) {
                    dispatch(
                        Msg.RegistrySuccess(
                            paymentUrl = result.paymentUrl,
                            message = result.message
                        )
                    )
                }
            }
        }
    }


    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when (message) {
                is Msg.InfoFailed -> State(
                    isError = true,
                    message = message.message
                )

                is Msg.InfoLoaded -> State(startStatement = message.startStatement)
                is Msg.Loading -> State(isLoading = true)
                is Msg.OnValueChanged -> State(startStatement = message.startStatement)
                is Msg.RegistryFailed -> copy(
                    isLoading = false,
                    registrationFailedEvent = triggered(message.message),
                    message = message.message
                )

                is Msg.RegistryLoading -> copy(isLoading = true)
                is Msg.RegistrySuccess -> copy(
                    isLoading = false,
                    registrationSucceededEvent = triggered(message.message),
                    message = message.paymentUrl
                )

                is Msg.OnConsumedFailedEvent -> copy(registrationFailedEvent = consumed())
                is Msg.OnConsumedSucceededEvent -> copy(registrationSucceededEvent = consumed())
            }
    }
}
