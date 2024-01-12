package com.markettwits.start_filter.starts.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.start_filter.start_filter.data.StartFilterRepository
import com.markettwits.start_filter.start_filter.presentation.StartFilterUi
import com.markettwits.start_filter.starts.store.StartsFilteredStore.Intent
import com.markettwits.start_filter.starts.store.StartsFilteredStore.Label
import com.markettwits.start_filter.starts.store.StartsFilteredStore.State
import com.markettwits.starts.StartsListItem
import kotlinx.coroutines.launch

interface StartsFilteredStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class Launch(val request: StartFilterUi) : Intent
        data class OnItemClick(val itemId: Int) : Intent
        data object OnClickBack : Intent
    }

    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val message: String = "",
        val starts: List<StartsListItem> = emptyList()
    )

    sealed interface Label {
        data object OnClickBack : Label
        data class OnItemClick(val id: Int) : Label
        data object Launch : Label
    }
}

class StartsFilteredStoreFactory(
    private val storeFactory: StoreFactory,
    private val startFilterRepository: StartFilterRepository
) {

    fun create(): StartsFilteredStore =
        object : StartsFilteredStore, Store<Intent, State, Label> by storeFactory.create(
            name = "StartsFilteredStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}


    private sealed interface Msg {
        data object Loading : Msg
        data class InfoLoaded(val starts: List<StartsListItem>) : Msg
        data class InfoFailed(val message: String) : Msg
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Unit, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.OnClickBack -> publish(StartsFilteredStore.Label.OnClickBack)
                is Intent.OnItemClick -> publish(StartsFilteredStore.Label.OnItemClick(intent.itemId))
                is Intent.Launch -> launch(intent.request)
            }
        }
        override fun executeAction(action: Unit, getState: () -> State) = Unit
        private fun launch(state: StartFilterUi) {
            scope.launch {
                dispatch(Msg.Loading)
                startFilterRepository.starts(state)
                    .onFailure {
                        dispatch(Msg.InfoFailed(it.message.toString()))
                    }
                    .onSuccess {
                        dispatch(Msg.InfoLoaded(it))
                    }
            }
        }
    }


    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when (message) {
                is Msg.InfoFailed -> State(isError = true, message = message.message)
                is Msg.InfoLoaded -> State(starts = message.starts)
                is Msg.Loading -> State(isLoading = true)
            }
    }
}
