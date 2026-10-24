package com.markettwits.sportsouce.profile.results.presentation.actions

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.*
import com.markettwits.sportsouce.profile.results.domain.ResultCategory
import com.markettwits.sportsouce.profile.results.domain.ResultsActionsRepository
import kotlinx.coroutines.*

@OptIn(ExperimentalCoroutinesApi::class)
class ResultsActionComponent internal constructor(context: ComponentContext, factory: StoreFactory,
    api: ResultsActionsRepository, action: ResultsAction, val categories: List<ResultCategory>,
    close: () -> Unit, changed: () -> Unit,
) : ComponentContext by context {
    private val store = instanceKeeper.getStore { resultsActionStore(factory, api, action, categories) }
    val state = store.stateFlow
    fun accept(intent: ResultsActionStore.Intent) = store.accept(intent)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    init {
        lifecycle.doOnDestroy { scope.cancel() }
        scope.launch { store.labels.collect { when (it) {
            ResultsActionStore.Label.Close -> close()
            ResultsActionStore.Label.Changed -> changed()
        } } }
    }
}
