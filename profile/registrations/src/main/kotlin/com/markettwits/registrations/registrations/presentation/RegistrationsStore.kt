package com.markettwits.registrations.registrations.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.registrations.registrations.data.RegistrationsDataSource
import com.markettwits.registrations.registrations.domain.StartPaymentState
import com.markettwits.registrations.registrations.domain.StartsStateInfo
import com.markettwits.registrations.registrations.presentation.RegistrationsStore.Intent
import com.markettwits.registrations.registrations.presentation.RegistrationsStore.Label
import com.markettwits.registrations.registrations.presentation.RegistrationsStore.State
import kotlinx.coroutines.launch

interface RegistrationsStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object Pop : Intent
        data object LoadData : Intent
        data class ShowPaymentDialog(val state: StartPaymentState) : Intent
        data class OnCLickItem(val itemId: Int) : Intent
    }

    data class State(
        val info: List<StartsStateInfo>,
        val paymentState: StartPaymentState = StartPaymentState(),
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val message: String = ""
    )

    sealed interface Label {
        data object GoBack : Label
        data class ShowPaymentDialog(val startPaymentState: StartPaymentState) : Label
        data class OnItemClick(val itemId: Int) : Label
    }
}

class RegistrationsDataStoreFactory(
    private val storeFactory: StoreFactory,
    private val dataSource: RegistrationsDataSource
) {

    fun create(): RegistrationsStore =
        object : RegistrationsStore, Store<Intent, State, Label> by storeFactory.create(
            name = "RegistrationsDataStore",
            initialState = State(emptyList()),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Msg {
        data object Loading : Msg
        data class InfoLoaded(
            val pokemonInfo: List<StartsStateInfo>,
            val paymentState: StartPaymentState
        ) : Msg

        data class InfoFailed(val message: String) : Msg
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Unit, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.LoadData -> {
                    launch()
                }
                is Intent.OnCLickItem -> publish(Label.OnItemClick(intent.itemId))
                is Intent.Pop -> publish(Label.GoBack)
                is Intent.ShowPaymentDialog -> publish(Label.ShowPaymentDialog(intent.state))
            }
        }

        override fun executeAction(action: Unit, getState: () -> State) {
            launch()
        }

        private fun launch() {
            scope.launch {
                dispatch(Msg.Loading)
                dataSource.registrations()
                    .onFailure {
                        dispatch(Msg.InfoFailed(it.message.toString()))
                    }
                    .onSuccess {
                        dispatch(Msg.InfoLoaded(it.info, it.paymentState))
                    }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State {
            return when (msg) {
                is Msg.InfoFailed -> State(
                    info = emptyList(),
                    isError = true,
                    message = msg.message
                )
                is Msg.InfoLoaded -> State(info = msg.pokemonInfo, paymentState = msg.paymentState)
                is Msg.Loading -> copy(isLoading = true)
            }
        }
    }
}
