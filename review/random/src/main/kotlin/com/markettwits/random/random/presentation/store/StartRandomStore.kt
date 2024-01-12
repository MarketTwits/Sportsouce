package com.markettwits.random.random.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.markettwits.random.random.data.RandomRepository
import com.markettwits.random.random.presentation.store.StartRandomStore.Intent
import com.markettwits.random.random.presentation.store.StartRandomStore.Label
import com.markettwits.random.random.presentation.store.StartRandomStore.State
import kotlinx.coroutines.launch

interface StartRandomStore : Store<Intent, State, Label> {

    sealed interface Intent{
        data object Retry : Intent
    }


    data class State(
        val isLoading : Boolean = false,
        val isError : Boolean = false,
        val message : String = "",
        val data : Int = 0
    )

    sealed interface Label {
        data class OpenStart(val startId : Int) : Label
    }
}
internal class StartRandomStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: RandomRepository
) {

    fun create(): StartRandomStore =
        object : StartRandomStore, Store<Intent, State, Label> by storeFactory.create(
            name = "StartRandomStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Msg {
        data object Loading : Msg
        data class InfoLoaded(val id: Int) : Msg
        data class InfoFailed(val message: String) : Msg
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Unit, State, Msg, Label>() {

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when(intent){
                Intent.Retry -> launch()
            }
        }
        private fun launch(){
            scope.launch {
                dispatch(Msg.Loading)
                repository.randomStart().onFailure {
                    dispatch(Msg.InfoFailed(it.message.toString()))
                }.onSuccess {
                    publish(StartRandomStore.Label.OpenStart(it))
                    dispatch(Msg.InfoLoaded(it))
                }
            }
        }
        override fun executeAction(action: Unit, getState: () -> State) {
            launch()
        }

    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when (message) {
               is Msg.InfoFailed -> State(isError = true, message = message.message)
                is Msg.InfoLoaded -> State(data = message.id)
                is Msg.Loading -> State(isLoading = true)
            }
    }
}