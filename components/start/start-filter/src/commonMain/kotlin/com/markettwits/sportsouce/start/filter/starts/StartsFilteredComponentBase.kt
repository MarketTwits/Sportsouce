package com.markettwits.sportsouce.start.filter.starts

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.sportsouce.start.filter.start_filter.domain.StartFilter
import com.markettwits.sportsouce.start.filter.start_filter.presentation.component.StartFilterUi
import com.markettwits.sportsouce.start.filter.starts.store.StartsFilteredStore
import com.markettwits.sportsouce.start.filter.starts.store.StartsFilteredStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class StartsFilteredComponentBase(
    context: ComponentContext,
    private val request: StartFilterUi,
    private val startFilterSorted: StartFilter.Sorted,
    private val storeFactory: StartsFilteredStoreFactory,
    private val pop: () -> Unit,
    private val onItemClick: (itemId: Int) -> Unit
) :
    StartsFilteredComponent, ComponentContext by context {

    private val store = instanceKeeper.getStore {
        storeFactory.create(request, startFilterSorted)
    }
    private val scope = CoroutineScope(Dispatchers.Main)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val value: StateFlow<StartsFilteredStore.State> = store.stateFlow

    override fun obtainEvent(event: StartsFilteredStore.Intent) {
        store.accept(event)
    }

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is StartsFilteredStore.Label.OnClickBack -> pop()
                    is StartsFilteredStore.Label.OnItemClick -> onItemClick(it.id)
                }
            }
        }
    }
}