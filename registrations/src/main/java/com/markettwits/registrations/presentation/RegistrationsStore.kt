package com.markettwits.registrations.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.registrations.data.RegistrationsDataSource
import com.markettwits.registrations.presentation.RegistrationsStore.Intent
import com.markettwits.registrations.presentation.RegistrationsStore.Label
import com.markettwits.registrations.presentation.RegistrationsStore.State
import com.markettwits.starts.StartsListItem
import kotlinx.coroutines.launch

interface RegistrationsStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object Pop : Intent
        data object LoadData : Intent
        data class OnCLickItem(val itemId: Int) : Intent
    }

    data class State(
        val info: List<StartsStateInfo>,
        val paymentList: List<StartsStateInfo> = emptyList(),
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val message: String = ""
    )

    data class StartsStateInfo(
        val id: Int,
        val name: String,
        val image: String,
        val dateStart: String,
        val statusCode: StartsListItem.StatusCode,
        val team: String,
        val payment: Boolean,
        val ageGroup: String,
        val distance: String,
        val member: String,
        val kindOfSport: String,
        val startTitle: String,
        val cost: String,
    )

    sealed interface Label {
        data object GoBack : Label
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

    sealed interface Action

    private sealed interface Msg {
        data object Loading : Msg
        data class InfoLoaded(
            val pokemonInfo: List<RegistrationsStore.StartsStateInfo>,
            val paymentList: List<RegistrationsStore.StartsStateInfo>
        ) : Msg

        data class InfoFailed(val message: String) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {

            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Unit, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.LoadData -> {
                    launch()
                }

                is Intent.OnCLickItem -> publish(Label.OnItemClick(intent.itemId))
                is Intent.Pop -> publish(Label.GoBack)
            }
        }

        override fun executeAction(action: Unit, getState: () -> State) {
            launch()
        }

        fun launch() {
            scope.launch {
                dispatch(Msg.Loading)
                val state = dataSource.registrations()
                state
                    .onFailure {
                        dispatch(Msg.InfoFailed(it.message.toString()))
                    }
                    .onSuccess {
                        dispatch(Msg.InfoLoaded(it.info, it.paymentList))
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
                is Msg.InfoLoaded -> State(info = msg.pokemonInfo, paymentList = msg.paymentList)
                Msg.Loading -> State(info = emptyList(), isLoading = true)
            }
        }
    }
}
