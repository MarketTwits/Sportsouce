package com.markettwits.sportsouce.start.filter.start_filter.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.sportsouce.start.filter.start_filter.domain.StartFilter
import com.markettwits.sportsouce.start.filter.start_filter.presentation.store.StartFilerStoreFactory
import com.markettwits.sportsouce.start.filter.start_filter.presentation.store.StartFilterStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class StartFilterComponentBase(
    context: ComponentContext,
    private val storeFactory: StartFilerStoreFactory,
    private val pop: () -> Unit,
    private val show: (StartFilterUi, StartFilter.Sorted) -> Unit,
) : StartFilterComponent, ComponentContext by context {

    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }
    private val scope = CoroutineScope(Dispatchers.Main)
    override val value: StateFlow<StartFilterStore.State> = store.stateFlow
    override fun obtainEvent(event: StartFilterStore.Intent) {
        store.accept(event)
    }

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is StartFilterStore.Label.GoBack -> pop()
                    is StartFilterStore.Label.Apply -> show(it.item, it.sorted)
                }
            }
        }
    }
}