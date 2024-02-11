package com.markettwits.start_search.search.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.start_search.search.data.StartsSearchRepository
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
            is Intent.OnClickStart -> publish(Label.OnClickStart(intent.id))
        }
    }

    private fun starts(value: String) {
        dispatch(Message.ChangeTextFiled(value))
        scope.launch {
            repository.starts(value).fold(
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
