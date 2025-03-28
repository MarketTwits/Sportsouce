package com.markettwits.sportsouce.starts.popular.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core.errors.api.throwable.networkExceptionHandler
import com.markettwits.sportsouce.starts.popular.domain.StartsPopularRepository
import com.markettwits.sportsouce.starts.popular.presentation.store.StartsPopularStore.Intent
import com.markettwits.sportsouce.starts.popular.presentation.store.StartsPopularStore.Label
import com.markettwits.sportsouce.starts.popular.presentation.store.StartsPopularStore.Message
import com.markettwits.sportsouce.starts.popular.presentation.store.StartsPopularStore.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

internal class StartsPopularExecutor(
    private val repository: StartsPopularRepository
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.OnClickStart -> publish(Label.OnClickStart(intent.id))
            is Intent.Retry -> launch()
            is Intent.OnClickBack -> publish(Label.OnClickBack)
        }
    }

    override fun executeAction(action: Unit) {
        launch()
    }

    private fun launch() {
        scope.launch {
            dispatch(Message.Loading)
            repository.popularStarts()
                .catch {
                    dispatch(Message.Failed(it.networkExceptionHandler().message.toString()))
                }
                .collect {
                    dispatch(Message.Loaded(it))
                }
        }
    }
}
