package com.markettwits.start.presentation.registration.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.start.data.registration.RegistrationStartRepository
import com.markettwits.start.domain.StartStatement
import com.markettwits.start.presentation.registration.store.StartRegistrationStore.Intent
import com.markettwits.start.presentation.registration.store.StartRegistrationStore.Label
import com.markettwits.start.presentation.registration.store.StartRegistrationStore.State
import kotlinx.coroutines.launch

interface StartRegistrationStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class ChangeFiled(val startStatement: StartStatement) : Intent
        data object GoBack : Intent
    }

    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val message: String = "",
        val startStatement: StartStatement? = null
    )

    sealed interface Label {
        data object GoBack : Label
    }
}

class StartRegistrationStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: RegistrationStartRepository
) {

    fun create(): StartRegistrationStore =
        object : StartRegistrationStore, Store<Intent, State, Label> by storeFactory.create(
            name = "RegistrationStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

    private sealed interface Msg {
        data object Loading : Msg
        data class InfoLoaded(val startStatement: StartStatement) : Msg
        data class InfoFailed(val message: String) : Msg
        data class OnValueChanged(val startStatement: StartStatement) : Msg
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Unit, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when(intent){
                is Intent.ChangeFiled -> dispatch(Msg.OnValueChanged(intent.startStatement))
                is Intent.GoBack -> publish(StartRegistrationStore.Label.GoBack)
            }
        }

        override fun executeAction(action: Unit, getState: () -> State) = launch()

        private fun launch() {
            dispatch(Msg.Loading)
            scope.launch {
                val result = repository.preload()
                result.onSuccess {
                    dispatch(Msg.InfoLoaded(it))
                }.onFailure {
                    dispatch(Msg.InfoFailed(it.message.toString()))
                }
            }

        }
    }


    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when (message) {
                is Msg.InfoFailed -> State(isError = true, message = message.message)
                is Msg.InfoLoaded -> State(startStatement = message.startStatement)
                is Msg.Loading -> State(isLoading = true)
                is Msg.OnValueChanged -> State(startStatement = message.startStatement)
            }
    }
}
