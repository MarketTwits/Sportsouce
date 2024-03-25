package com.markettwits.schedule.schedule.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.cloud.exception.networkExceptionHandler
import com.markettwits.schedule.schedule.domain.ScheduleRepository
import com.markettwits.schedule.schedule.presentation.store.StartsScheduleStore.Intent
import com.markettwits.schedule.schedule.presentation.store.StartsScheduleStore.Label
import com.markettwits.schedule.schedule.presentation.store.StartsScheduleStore.State
import com.markettwits.starts_common.domain.StartsListItem
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


internal class StartsScheduleStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: ScheduleRepository
) {

    fun create(): StartsScheduleStore =
        object : StartsScheduleStore, Store<Intent, State, Label> by storeFactory.create(
            name = "StartsScheduleStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Msg {
        data object Loading : Msg
        data class InfoLoadedFull(val data: List<StartsListItem>) : Msg
        data class InfoFailed(val message: String) : Msg
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Unit, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.Launch -> launch()
                is Intent.OnClickItem -> publish(Label.OnClickItem(intent.item))
                is Intent.Back -> publish(Label.Back)
            }
        }

        override fun executeAction(action: Unit, getState: () -> State) {
            launch()
        }

        private fun launch() {
            scope.launch {
                dispatch(Msg.Loading)
                repository.schedule()
                    .catch {
                        dispatch(Msg.InfoFailed(networkExceptionHandler(it).message.toString()))
                    }
                    .collect {
                        dispatch(Msg.InfoLoadedFull(data = it))
                    }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when (message) {
                is Msg.InfoFailed -> State(isError = true, message = message.message)
                is Msg.Loading -> State(isLoading = true)
                is Msg.InfoLoadedFull -> State(
                    actualStarts = message.data,
                )
            }
    }
}
