package com.markettwits.start_search.search.presentation.component


import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.start_search.filter.presentation.component.StartFilterUi
import com.markettwits.start_search.search.presentation.store.StartsSearchStore
import com.markettwits.start_search.search.presentation.store.StartsSearchStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StartsSearchComponentBase(
    componentContext: ComponentContext,
    private val storeFactory: StartsSearchStoreFactory,
    private val back: () -> Unit,
    private val filter: (StartFilterUi) -> Unit,
    private val start: (Int) -> Unit
) : StartsSearchComponent,
    ComponentContext by componentContext {
    private val scope = CoroutineScope(Dispatchers.Main.immediate)
    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }
    override val model: StateFlow<StartsSearchStore.State> = store.stateFlow
    override fun obtainEvent(intent: StartsSearchStore.Intent) {
        store.accept(intent)
    }

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is StartsSearchStore.Label.OnClickBack -> back()
                    is StartsSearchStore.Label.OnClickFilter -> filter(it.filter)
                    is StartsSearchStore.Label.OnClickStart -> start(it.id)
                }
            }
        }
    }
}
