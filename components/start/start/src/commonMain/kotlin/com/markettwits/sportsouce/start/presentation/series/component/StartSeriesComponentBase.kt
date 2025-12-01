package com.markettwits.sportsouce.start.presentation.series.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.core.decompose.componentScope
import com.markettwits.sportsouce.start.presentation.series.store.StartSeriesStore
import com.markettwits.sportsouce.start.presentation.series.store.StartSeriesStoreFactory
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class StartSeriesComponentBase(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    items: List<StartsListItem>,
    currentStartId: Int,
    private val onClickBack: () -> Unit,
    private val onClickStart: (StartsListItem) -> Unit,
) : StartSeriesComponent, ComponentContext by componentContext {

    private val store: StartSeriesStore = instanceKeeper.getStore {
        StartSeriesStoreFactory(storeFactory).create(
            items = items,
            currentStartId = currentStartId,
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<StartSeriesStore.State> = store.stateFlow

    override fun obtainEvent(intent: StartSeriesStore.Intent) {
        store.accept(intent)
    }

    init {
        store.labels.onEach { label ->
            when (label) {
                StartSeriesStore.Label.OnClickBack -> onClickBack()
                is StartSeriesStore.Label.OnClickStart -> {
                    val start = items.find { it.id == label.startId }
                    if (start != null) {
                        onClickStart(start)
                    }
                }
            }
        }.launchIn(componentScope)
    }
}
