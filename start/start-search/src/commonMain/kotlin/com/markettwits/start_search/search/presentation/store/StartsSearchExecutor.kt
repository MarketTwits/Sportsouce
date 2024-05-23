package com.markettwits.start_search.search.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.cloud.exception.networkExceptionHandler
import com.markettwits.start_search.filter.domain.StartFilter
import com.markettwits.start_search.filter.presentation.component.StartFilterUi
import com.markettwits.start_search.search.data.repository.StartsSearchRepository
import com.markettwits.start_search.search.domain.CoroutineDebounceBase
import com.markettwits.start_search.search.domain.StartsSearch
import com.markettwits.start_search.search.presentation.store.StartsSearchStore.Intent
import com.markettwits.start_search.search.presentation.store.StartsSearchStore.Label
import com.markettwits.start_search.search.presentation.store.StartsSearchStore.Message
import com.markettwits.start_search.search.presentation.store.StartsSearchStore.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class StartsSearchExecutor(private val repository: StartsSearchRepository) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {

    private val debounce by CoroutineDebounceBase(scope)

    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.ChangeTextFiled -> onValueChanged(getState(), intent.value)
            is Intent.OnClickBack -> publish(Label.OnClickBack)
            is Intent.OnClickBrushText -> onClickBrush(getState())
            is Intent.OnClickFilter -> publish(Label.OnClickFilter(getState().filter))
            is Intent.OnClickStart -> onClickStart(intent.id, intent.startTitle)
            is Intent.OnClickHistoryItem -> {
                onValueChanged(getState(), intent.value)
            }

            is Intent.OnFilterApply -> {
                dispatch(Message.FilterApply(intent.filter, intent.sorted))
                starts(
                    getState().sorted,
                    getState().filter,
                    getState().query,
                )
            }

            is Intent.OnClickRemoveFilter -> onClickRemoveFilter(getState())
            is Intent.OnClickRetry -> starts(
                getState().sorted,
                getState().filter,
                getState().query,
            )
        }
    }

    override fun executeAction(action: Unit, getState: () -> State) {
        scope.launch {
            repository.history().collect {
                dispatch(Message.InfoLoaded(StartsSearch(it, emptyList())))
            }
        }
    }

    private fun onClickStart(startId: Int, startTitle: String) {
        scope.launch {
            repository.addToHistory(startTitle)
            publish(Label.OnClickStart(startId))
        }
    }

    private fun onClickBrush(state: State) {
        if (state.filter.filterIsEmpty()) {
            dispatch(Message.Brush(true))
        } else {
            dispatch(Message.Brush(false))
            starts(
                state.sorted,
                state.filter,
                "",
            )
        }
    }

    private fun onClickRemoveFilter(state: State) {
        val modifiedItems = state.filter.items.map { filterGroupItem ->
            filterGroupItem.copy(selected = emptyList())
        }
        val newState = state.filter.copy(items = modifiedItems)
        dispatch(
            Message.FilterApply(
                filter = newState,
                sorted = state.sorted
            )
        )
        if (state.query.isBlank()) {
            dispatch(Message.Brush(true))
        } else {
            starts(
                state.sorted,
                newState,
                state.query,
            )
        }
    }

    private fun onValueChanged(state: State, newValue: String) {
        dispatch(Message.ChangeTextFiled(newValue))
        //debounce.debounce(newValue, delay = 500L){
        starts(
            sorted = state.sorted,
            filterUi = state.filter,
            value = newValue,
        )
        // }
    }

    private fun starts(
        sorted: StartFilter.Sorted,
        filterUi: StartFilterUi,
        value: String,
    ) {
        scope.launch {
            repository.search(filterUi, sorted, value)
                .onStart {
                    dispatch(Message.Loading)
                }
                .catch {
                    dispatch(Message.InfoFailed(networkExceptionHandler(it).message.toString()))
                }.collect {
                    dispatch(Message.InfoLoaded(it))
                }
        }
    }
}

