package com.markettwits.start.presentation.registration.store

import android.util.Log
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.cloud.model.auth.common.AuthErrorResponse
import com.markettwits.core_ui.event.EventContent
import com.markettwits.core_ui.event.consumed
import com.markettwits.core_ui.event.triggered
import com.markettwits.start.data.registration.RegistrationStartRepository
import com.markettwits.start.domain.StartStatement
import com.markettwits.start.presentation.registration.store.StartRegistrationStore.State
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.launch


class StartRegistrationStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: RegistrationStartRepository
) {

    fun create(
        distanceInfo: DistanceItem,
        starId: Int,
        paymentDisabled: Boolean
    ): StartRegistrationStore =
        object : StartRegistrationStore,
            Store<StartRegistrationStore.Intent, State, StartRegistrationStore.Label> by storeFactory.create(
                name = "RegistrationStore",
                initialState = State(),
                bootstrapper = BootstrapperImpl(distanceInfo, paymentDisabled),
                executorFactory = { ExecutorImpl(distanceInfo, starId) },
                reducer = ReducerImpl
            ) {}

    private sealed interface Action {
        data object Loading : Action
        data class InfoLoaded(val startStatement: StartStatement) : Action
        data class InfoFailed(val message: String) : Action
    }

    private inner class BootstrapperImpl(
        private val distanceInfo: DistanceItem,
        private val paymentDisabled: Boolean
    ) :
        CoroutineBootstrapper<Action>() {
        override fun invoke() {
            launch(distanceInfo, paymentDisabled)
        }

        private fun launch(distanceInfo: DistanceItem, paymentDisabled: Boolean) {
            dispatch(Action.Loading)
            scope.launch {

                repository.preload(distanceInfo, paymentDisabled).fold(
                    onSuccess = { dispatch(Action.InfoLoaded(it)) },
                    onFailure = {
                        val message = when (it) {
                            is ClientRequestException -> it.response.body<AuthErrorResponse>().message
                            else -> it.message.toString()
                        }
                        dispatch(Action.InfoFailed(message))
                    }
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
        data class SnackbarMessage(val message: String, val value: String, val success: Boolean) :
            Msg

        data object RegistryLoading : Msg
        data object OnConsumedSucceededEvent : Msg
        data object OnConsumedFailedEvent : Msg
        data object OnConsumedEvent : Msg
    }

    private inner class ExecutorImpl(
        private val distanceItem: DistanceItem,
        private val starId: Int
    ) :
        CoroutineExecutor<StartRegistrationStore.Intent, Action, State, Msg, StartRegistrationStore.Label>() {
        override fun executeIntent(intent: StartRegistrationStore.Intent, getState: () -> State) {
            when (intent) {
                is StartRegistrationStore.Intent.ChangeFiled -> dispatch(Msg.OnValueChanged(intent.startStatement))
                is StartRegistrationStore.Intent.GoBack -> publish(StartRegistrationStore.Label.GoBack)
                is StartRegistrationStore.Intent.OnClickPay -> save(
                    statement = getState().startStatement!!,
                    distanceItem = distanceItem,
                    starId = starId,
                    withPayment = true
                )

                is StartRegistrationStore.Intent.OnClickSave -> save(
                    statement = getState().startStatement!!,
                    distanceItem = distanceItem,
                    starId = starId,
                    withPayment = false
                )

                is StartRegistrationStore.Intent.ChangePromo -> applyPromo(
                    getState().startStatement!!,
                    intent.value,
                    starId
                )

                is StartRegistrationStore.Intent.OnConsumedEvent -> dispatch(Msg.OnConsumedEvent)
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.InfoFailed -> dispatch(Msg.InfoFailed(action.message))
                is Action.InfoLoaded -> dispatch(Msg.InfoLoaded(action.startStatement))
                is Action.Loading -> dispatch(Msg.Loading)
            }
        }

        private fun calculatePrice(startStatement: StartStatement, percent: Int): StartStatement {
            val price = startStatement.price.toInt()
            val updatePrice = price.div(percent)
            return startStatement.copy(price = updatePrice.toString())
        }

        private fun applyPromo(startStatement: StartStatement, value: String, startId: Int) {
            scope.launch {
                repository.promo(value, startId).fold(
                    onFailure = {
                        dispatch(Msg.SnackbarMessage("Промокод не действителен", "", false))
                    },
                    onSuccess = {
                        val newState = calculatePrice(startStatement, it.discountPercent)
                        dispatch(Msg.OnValueChanged(newState))
                        dispatch(Msg.SnackbarMessage("Промокод успешно применен", "", true))
                        Log.e("mt05", "УСПЕХ")
                    }
                )
            }
        }


        private fun save(
            withPayment: Boolean,
            statement: StartStatement,
            distanceItem: DistanceItem,
            starId: Int
        ) {
            scope.launch {
                dispatch(Msg.RegistryLoading)
                val result =
                    repository.registry(
                        withoutPayment = !withPayment,
                        statement = statement,
                        distanceInfo = distanceItem,
                        startId = starId
                    )
                result.fold(onSuccess = {
                    dispatch(
                        Msg.RegistrySuccess(
                            paymentUrl = it.paymentUrl,
                            message = it.message
                        )
                    )
                }, onFailure = {
                    dispatch(Msg.RegistryFailed(it.message.toString()))
                })
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
                    event = triggered(EventContent(success = false, message = message.message)),
                    message = message.message
                )

                is Msg.RegistryLoading -> copy(isLoading = true)
                is Msg.RegistrySuccess -> copy(
                    isLoading = false,
                    event = triggered(EventContent(success = true, message = message.message)),
                    message = message.paymentUrl
                )

                is Msg.OnConsumedFailedEvent -> copy(event = consumed())
                is Msg.OnConsumedSucceededEvent -> copy(event = consumed())
                is Msg.SnackbarMessage -> copy(
                    event = triggered(
                        EventContent(
                            success = message.success,
                            message = message.message
                        )
                    )
                )

                Msg.OnConsumedEvent -> copy(event = consumed())
            }
    }
}
