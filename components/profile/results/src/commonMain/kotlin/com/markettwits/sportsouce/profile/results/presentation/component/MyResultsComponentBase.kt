package com.markettwits.sportsouce.profile.results.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.*
import com.markettwits.sportsouce.profile.results.presentation.actions.*
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.*
import com.markettwits.getOrCreateKoinScope
import com.markettwits.sportsouce.profile.results.di.myResultsModule
import com.markettwits.sportsouce.profile.results.presentation.store.*
import kotlinx.coroutines.*

@OptIn(ExperimentalCoroutinesApi::class)
class MyResultsComponentBase(
    componentContext: ComponentContext,
    private val back: () -> Unit,
    private val openStart: (Int) -> Unit,
    private val openResult: (Int) -> Unit = openStart,
) : MyResultsComponent, ComponentContext by componentContext {
    private val dependencies = getOrCreateKoinScope(listOf(myResultsModule))
    private val store = instanceKeeper.getStore { dependencies.get<MyResultsStoreFactory>().create() }
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val actionNavigation = SlotNavigation<ResultsAction>()
    override val actionSlot = childSlot(source = actionNavigation, serializer = ResultsAction.serializer(), key = "results-action",
        handleBackButton = false) { action, context ->
        ResultsActionComponent(context, dependencies.get(), dependencies.get(), action, store.state.summary?.categories.orEmpty(),
            close = { actionNavigation.dismiss() }, changed = { store.accept(MyResultsStore.Intent.Refresh) })
    }
    override fun openAction(action: ResultsAction) { actionNavigation.activate(action) }
    override val state = store.stateFlow
    override fun obtainEvent(intent: MyResultsStore.Intent) = store.accept(intent)
    init {
        lifecycle.doOnDestroy { scope.cancel() }
        scope.launch { store.labels.collect {
            when (it) { MyResultsStore.Label.Back -> back(); is MyResultsStore.Label.OpenStart -> openStart(it.id); is MyResultsStore.Label.OpenResult -> openResult(it.id) }
        } }
    }
}
