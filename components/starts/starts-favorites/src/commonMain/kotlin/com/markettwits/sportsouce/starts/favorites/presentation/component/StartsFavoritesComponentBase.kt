package com.markettwits.sportsouce.starts.favorites.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import com.markettwits.sportsouce.starts.favorites.presentation.store.StartsFavoritesStoreFactory
import com.markettwits.sportsouce.starts.favorites.presentation.store.StartsPopularStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class StartsFavoritesComponentBase(
    componentContext: ComponentContext,
    private val storeFactory: StartsFavoritesStoreFactory,
    private val pop: () -> Unit,
    private val start: (StartsListItem) -> Unit,
) : StartsFavoritesComponent, ComponentContext by componentContext {

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
                    is StartsPopularStore.Label.OnClickStart -> start(it.startItem)
                }
            }
        }
    }
}