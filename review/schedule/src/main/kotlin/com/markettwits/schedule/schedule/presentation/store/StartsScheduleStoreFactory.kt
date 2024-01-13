package com.markettwits.schedule.schedule.presentation.store

import android.util.Log
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.schedule.schedule.data.ScheduleRepository
import com.markettwits.schedule.schedule.domain.StartsSchedule
import com.markettwits.schedule.schedule.presentation.store.StartsScheduleStore.Intent
import com.markettwits.schedule.schedule.presentation.store.StartsScheduleStore.Label
import com.markettwits.schedule.schedule.presentation.store.StartsScheduleStore.State
import com.markettwits.starts.presentation.StartsListItem
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
        data class InfoLoaded(val data: List<StartsSchedule>) : Msg
        data class InfoFailed(val message: String) : Msg
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Unit, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when(intent){
                is Intent.Launch -> launch()
                is Intent.OnClickItem -> publish(Label.OnClickItem(intent.item))
                is Intent.Back -> publish(Label.Back)
            }
        }

        override fun executeAction(action: Unit, getState: () -> State) {
            launch()
        }
        private fun launch(){
            scope.launch {
                dispatch(Msg.Loading)
                repository.starts()
                    .onFailure {
                        dispatch(Msg.InfoFailed(it.message.toString()))
                        Log.e("mt05", "#StartsScheduleStore FAILED :${it.message.toString()}")
                    }
                    .onSuccess {
                        dispatch(Msg.InfoLoaded(data = it))
                        Log.e("mt05", "#StartsScheduleStore SUCCESS :${it.toString()}")
                    }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when (message) {
                is Msg.InfoFailed -> StartsScheduleStore.State(isError = true, message = message.message)
                is Msg.Loading -> StartsScheduleStore.State(isLoading = true)
                is Msg.InfoLoaded -> StartsScheduleStore.State(
                    starts = message.data,
                )
            }
    }
}
