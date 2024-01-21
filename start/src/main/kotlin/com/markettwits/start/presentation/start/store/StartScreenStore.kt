package com.markettwits.start.presentation.start.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.cloud.ext_model.DistanceInfo
import com.markettwits.core_ui.base_extensions.retryRunCatchingAsync
import com.markettwits.start.data.start.StartDataSource
import com.markettwits.start.presentation.membres.list.StartMembersUi
import com.markettwits.start.presentation.start.StartItemUi
import com.markettwits.start.presentation.start.store.StartScreenStore.Intent
import com.markettwits.start.presentation.start.store.StartScreenStore.Label
import com.markettwits.start.presentation.start.store.StartScreenStore.State
import kotlinx.coroutines.launch

interface StartScreenStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class OnClickMembers(val members: List<StartMembersUi>) : Intent
        data class OnClickDistance(val distanceInfo: DistanceInfo) : Intent
        data object OnClickBack : Intent
        data object OnClickRetry : Intent
    }

    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val message: String = "",
        val data: StartItemUi.StartItemUiSuccess? = null
    )

    sealed interface Label {
        data class OnClickMembers(val members: List<StartMembersUi>) : Label
        data class OnClickDistance(val distanceInfo: DistanceInfo) : Label
        data object OnClickBack : Label
    }
}

class StartScreenStoreFactory(
    private val storeFactory: StoreFactory,
    private val service: StartDataSource,
) {
    fun create(startID: Int): StartScreenStore =
        object : StartScreenStore, Store<Intent, State, Label> by storeFactory.create(
            name = "StartScreenStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { ExecutorImpl(startID) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Msg {
        data object Loading : Msg
        data class InfoLoaded(val data: StartItemUi.StartItemUiSuccess) : Msg
        data class InfoFailed(val message: String) : Msg
    }

    private inner class ExecutorImpl(private val startId: Int) :
        CoroutineExecutor<Intent, Unit, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.OnClickBack -> publish(StartScreenStore.Label.OnClickBack)
                is Intent.OnClickDistance -> publish(StartScreenStore.Label.OnClickDistance(intent.distanceInfo))
                is Intent.OnClickMembers -> publish(StartScreenStore.Label.OnClickMembers(intent.members))
                is Intent.OnClickRetry -> launch(startId, true)
            }
        }

        override fun executeAction(action: Unit, getState: () -> State) {
            launch(startId = startId, false)
        }

        private fun launch(startId: Int, relaunch: Boolean) {
            scope.launch {
                dispatch(Msg.Loading)
                service.start(startId, relaunch).fold(
                    onFailure = {
                        dispatch(Msg.InfoFailed(it.message.toString()))
                    },
                    onSuccess = {
                        dispatch(Msg.InfoLoaded(it))
                    }
                )
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when(message){
                is Msg.InfoFailed -> State(message = message.message, isError = true)
                is Msg.InfoLoaded -> State(data = message.data)
                is Msg.Loading -> copy(isLoading = true)
            }
    }
}
