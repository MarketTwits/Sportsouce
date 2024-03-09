package com.markettwits.start_search.search.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.start_search.search.data.StartsSearchRepository
import com.markettwits.start_search.search.domain.StartsSearch
import com.markettwits.start_search.search.presentation.store.StartsSearchStore.Intent
import com.markettwits.start_search.search.presentation.store.StartsSearchStore.Label
import com.markettwits.start_search.search.presentation.store.StartsSearchStore.Message
import com.markettwits.start_search.search.presentation.store.StartsSearchStore.State
import kotlinx.coroutines.launch

class StartsSearchExecutor(private val repository: StartsSearchRepository) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {
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
            val searches = repository.history()
            dispatch(Message.InfoLoaded(StartsSearch(searches, emptyList())))
        }
    }

    private fun onClickStart(startId: Int, startTitle: String) {
        scope.launch {
            publish(Label.OnClickStart(startId))
            repository.search(startTitle, true)
        }
    }

    private fun starts(value: String, done: Boolean = false) {
        dispatch(Message.ChangeTextFiled(value))
        scope.launch {
            repository.search(value, addToHistory = done).fold(
                onSuccess = {
                    dispatch(Message.InfoLoaded(it))
                },
                onFailure = {
                    dispatch(Message.InfoFailed(it.message.toString()))
                }
            )
        }
    }
}
