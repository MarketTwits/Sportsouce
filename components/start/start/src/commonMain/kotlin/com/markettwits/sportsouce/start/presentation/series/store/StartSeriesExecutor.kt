package com.markettwits.sportsouce.start.presentation.series.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core.log.LogTagProvider
import com.markettwits.sportsouce.start.presentation.series.store.StartSeriesStore.*

class StartSeriesExecutor : CoroutineExecutor<Intent, Unit, State, Message, Label>(), LogTagProvider {

    override val tag: String = "StartSeriesExecutor"

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.OnClickBack -> publish(Label.OnClickBack)
            is Intent.OnClickStart -> publish(Label.OnClickStart(intent.startId))
        }
    }

    override fun executeAction(action: Unit) {
        // No async operations
    }
}
