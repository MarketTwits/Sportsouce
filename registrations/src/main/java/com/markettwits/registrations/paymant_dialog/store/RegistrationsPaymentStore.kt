package com.markettwits.registrations.paymant_dialog.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core_ui.event.EventContent
import com.markettwits.core_ui.event.StateEventWithContent
import com.markettwits.core_ui.event.consumed
import com.markettwits.core_ui.event.triggered
import com.markettwits.registrations.paymant_dialog.store.RegistrationsPaymentStore.Intent
import com.markettwits.registrations.paymant_dialog.store.RegistrationsPaymentStore.Label
import com.markettwits.registrations.paymant_dialog.store.RegistrationsPaymentStore.State
import com.markettwits.registrations.registrations.data.RegistrationsDataSource
import com.markettwits.registrations.registrations.presentation.RegistrationsStore
import kotlinx.coroutines.launch

interface RegistrationsPaymentStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class OnClickPay(val id: Int) : Intent
    }

    data class State(
        val items: RegistrationsStore.StartPaymentState,
        val event: StateEventWithContent<String> = consumed()
    )

    sealed interface Label
}

class RegistrationsPaymentStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: RegistrationsDataSource
) {

    fun create(items: RegistrationsStore.StartPaymentState): RegistrationsPaymentStore =
        object : RegistrationsPaymentStore, Store<Intent, State, Label> by storeFactory.create(
            name = "RegistrationsPaymentStore",
            initialState = State(items),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

    private sealed interface Msg {
        data class Loaded(val url: String) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when(intent){
                is Intent.OnClickPay -> repay(intent.id)
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
        }
        private fun repay(id : Int){
            scope.launch {
                repository.pay(id).fold(
                    onSuccess = {
                       dispatch(Msg.Loaded(it))
                    },
                    onFailure = {}
                )
            }
        }
    }


    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when (message) {
                is Msg.Loaded -> copy(event = triggered(message.url))
            }
    }
}
