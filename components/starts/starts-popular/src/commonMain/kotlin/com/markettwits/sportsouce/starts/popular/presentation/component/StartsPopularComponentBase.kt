package com.markettwits.sportsouce.starts.popular.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.sportsouce.starts.popular.presentation.store.StartsPopularStore
import com.markettwits.sportsouce.starts.popular.presentation.store.StartsPopularStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class StartsPopularComponentBase(
    context: ComponentContext,
    private val storeFactory: StartsPopularStoreFactory,
    private val pop: () -> Unit,
    private val start: (Int) -> Unit
) : StartsPopularComponent, ComponentContext by context {

    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<StartsPopularStore.State> = store.stateFlow


    override fun obtainEvent(intent: StartsPopularStore.Intent) {
        store.accept(intent)
    }

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is StartsPopularStore.Label.OnClickBack -> pop()
                    is StartsPopularStore.Label.OnClickStart -> start(it.id)
                }
            }
        }
    }
}