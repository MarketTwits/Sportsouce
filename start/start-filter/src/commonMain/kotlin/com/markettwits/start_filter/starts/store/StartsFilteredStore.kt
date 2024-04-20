package com.markettwits.start_filter.starts.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.start_filter.start_filter.data.StartFilterRepository
import com.markettwits.start_filter.start_filter.domain.StartFilter
import com.markettwits.start_filter.start_filter.presentation.component.StartFilterUi
import com.markettwits.start_filter.starts.store.StartsFilteredStore.Intent
import com.markettwits.start_filter.starts.store.StartsFilteredStore.Label
import com.markettwits.start_filter.starts.store.StartsFilteredStore.State
import com.markettwits.starts_common.domain.StartsListItem
import kotlinx.coroutines.launch

interface StartsFilteredStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object Launch : Intent
        data class OnItemClick(val itemId: Int) : Intent
        data object OnClickBack : Intent
    }

    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val message: String = "",
        val starts: List<StartsListItem> = emptyList(),
        val sorted: StartFilter.Sorted = StartFilter.Sorted.FirstBefore
    )

    sealed interface Label {
        data object OnClickBack : Label
        data class OnItemClick(val id: Int) : Label
    }
}

internal class StartsFilteredStoreFactory(
    private val storeFactory: StoreFactory,
    private val startFilterRepository: StartFilterRepository
) {

    fun create(request: StartFilterUi, sorted: StartFilter.Sorted): StartsFilteredStore =
        object : StartsFilteredStore, Store<Intent, State, Label> by storeFactory.create(
            name = "StartsFilteredStore",
            initialState = State(),
            bootstrapper = BootstrapperImpl(request, sorted),
            executorFactory = { ExecutorImpl(request) },
            reducer = ReducerImpl
        ) {}


    private sealed interface Msg {
        data object Loading : Msg
        data class InfoLoaded(val starts: List<StartsListItem>) : Msg
        data class InfoFailed(val message: String) : Msg
    }

    sealed interface Action {
        data object Loading : Action
        data class InfoLoaded(val starts: List<StartsListItem>) : Action
        data class InfoFailed(val message: String) : Action
    }

    private inner class BootstrapperImpl(
        private val state: StartFilterUi,
        private val sorted: StartFilter.Sorted
    ) :
        CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                dispatch(Action.Loading)
                startFilterRepository.starts(state, sorted)
                    .onFailure {
                        dispatch(Action.InfoFailed(it.message.toString()))
                    }
                    .onSuccess {
                        dispatch(Action.InfoLoaded(it))
                    }
            }
        }

    }

    private inner class ExecutorImpl(
        private val state: StartFilterUi,
    ) : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.OnClickBack -> publish(Label.OnClickBack)
                is Intent.OnItemClick -> publish(Label.OnItemClick(intent.itemId))
                is Intent.Launch -> launch(state, getState().sorted)
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.InfoFailed -> dispatch(Msg.InfoFailed(action.message))
                is Action.InfoLoaded -> dispatch(Msg.InfoLoaded(action.starts))
                is Action.Loading -> dispatch(Msg.Loading)
            }
        }

        private fun launch(
            state: StartFilterUi,
            sorted: StartFilter.Sorted
        ) {
            scope.launch {
                dispatch(Msg.Loading)
                startFilterRepository.starts(state, sorted)
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
                is Msg.InfoFailed -> copy(
                    isError = true,
                    isLoading = false,
                    message = message.message
                )

                is Msg.InfoLoaded -> copy(isLoading = false, starts = message.starts)
                is Msg.Loading -> copy(isLoading = true)
            }
    }
}
