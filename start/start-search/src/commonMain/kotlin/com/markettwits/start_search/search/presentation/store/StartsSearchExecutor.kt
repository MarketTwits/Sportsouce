package com.markettwits.start_search.search.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.cloud.exception.networkExceptionHandler
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
            is Intent.ChangeTextFiled -> starts(intent.value)
            is Intent.OnClickBack -> publish(Label.OnClickBack)
            is Intent.OnClickBrushText -> dispatch(Message.Brush)
            is Intent.OnClickFilter -> publish(Label.OnClickFilter)
            is Intent.OnClickStart -> onClickStart(intent.id, intent.startTitle)
            is Intent.OnClickHistoryItem -> starts(intent.value)
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

    private fun starts(value: String, done: Boolean = false) {
        dispatch(Message.ChangeTextFiled(value))
        debounce.debounce {
            repository.search(value, addToHistory = done)
                .onStart {
                    dispatch(Message.Loading)
                }
                .catch {
                    dispatch(Message.InfoFailed(networkExceptionHandler(it).message.toString()))
                }
                .collect {
                    dispatch(Message.InfoLoaded(it))
                }
        }
    }
}
