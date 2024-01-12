package com.markettwits.start_filter.starts

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.start_filter.start_filter.presentation.StartFilterUi
import com.markettwits.start_filter.start_filter.presentation.store.StartFilterStore
import com.markettwits.start_filter.starts.store.StartsFilteredStore
import com.markettwits.start_filter.starts.store.StartsFilteredStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.KoinApplication.Companion.init

class StartsFilteredComponentBase(
    context: ComponentContext,
    private val request: StartFilterUi,
    private val storeFactory: StartsFilteredStoreFactory,
    private val pop: () -> Unit,
    private val onItemClick: (itemId: Int) -> Unit
) :
    StartsFilteredComponent, ComponentContext by context {

    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }
    private val scope = CoroutineScope(Dispatchers.Main)
    override val value: StateFlow<StartsFilteredStore.State> = store.stateFlow
    override fun obtainEvent(event: StartsFilteredStore.Intent) {
        store.accept(event)
    }

    init {
        obtainEvent(StartsFilteredStore.Intent.Launch(request))
        scope.launch {
            store.labels.collect {
                when(it){
                    is StartsFilteredStore.Label.Launch -> {}
                    is StartsFilteredStore.Label.OnClickBack -> pop()
                    is StartsFilteredStore.Label.OnItemClick -> onItemClick(it.id)
                }
            }
        }
    }
}