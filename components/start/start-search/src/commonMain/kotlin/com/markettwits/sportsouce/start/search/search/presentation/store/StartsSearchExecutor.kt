package com.markettwits.sportsouce.start.search.search.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.sportsouce.start.search.filter.domain.StartFilter
import com.markettwits.sportsouce.start.search.filter.presentation.component.StartFilterUi
import com.markettwits.sportsouce.start.search.search.data.repository.StartsSearchRepository
import com.markettwits.sportsouce.start.search.search.presentation.store.StartsSearchStore.*
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class StartsSearchExecutor(private val repository: StartsSearchRepository) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {

    private var searchJob: Job? = null

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.ChangeTextFiled -> onValueChanged(state(), intent.value)
            is Intent.OnClickBack -> publish(Label.OnClickBack)
            is Intent.OnClickBrushText -> onClickBrush(state())
            is Intent.OnClickFilter -> publish(Label.OnClickFilter(state().filter))
            is Intent.OnClickStart -> onClickStart(intent.startItem)
            is Intent.OnClickHistoryItem -> {
                onValueChanged(state(), intent.value)
            }

            is Intent.OnDeleteHistoryItem -> {
                onDeleteHistoryItem(intent.value)
            }

            is Intent.OnFilterApply -> {
                dispatch(Message.FilterApply(intent.filter, intent.sorted))
                starts(
                    state().sorted,
                    state().filter,
                    state().query,
                )
            }

            is Intent.OnClickRemoveFilter -> onClickRemoveFilter(state())
            is Intent.OnClickRetry -> starts(
                state().sorted,
                state().filter,
                state().query,
            )
        }
    }

    override fun executeAction(action: Unit) {
        scope.launch {
            repository.history().collect {
                dispatch(Message.HistoryLoaded(it))
            }
        }
    }

    private fun onClickStart(startListItem: StartsListItem) {
        scope.launch {
            repository.addToHistory(startListItem.name)
            publish(Label.OnClickStart(startListItem))
        }
    }

    private fun onDeleteHistoryItem(value: String) {
        scope.launch {
            repository.deleteFromHistory(value)
            dispatch(Message.DeleteHistoryItem(value))
        }
    }

    private fun onClickBrush(state: State) {
        dispatch(Message.Brush(state.filter.filterIsEmpty()))
        starts(state.sorted, state.filter, "")
    }

    private fun onClickRemoveFilter(state: State) {
        val filter = state.filter.cleared()
        val sorted = StartFilter.Sorted.Popular
        dispatch(Message.FilterApply(filter, sorted))
        starts(sorted, filter, state.query)
    }

    private fun onValueChanged(state: State, newValue: String) {
        dispatch(Message.ChangeTextFiled(newValue))
        starts(state.sorted, state.filter, newValue, debounce = true)
    }

    private fun starts(
        sorted: StartFilter.Sorted,
        filterUi: StartFilterUi,
        value: String,
        debounce: Boolean = false,
    ) {
        searchJob?.cancel()
        if (value.isBlank() && filterUi.filterIsEmpty()) {
            dispatch(Message.ClearResults)
            return
        }
        searchJob = scope.launch {
            if (debounce) delay(300L)
            repository.search(filterUi, sorted, value)
                .onStart {
                    dispatch(Message.Loading)
                }
                .catch {
                    dispatch(Message.InfoFailed(it.mapToSauceError()))
                }.collect {
                    dispatch(Message.InfoLoaded(it))
                }
        }
    }
}

