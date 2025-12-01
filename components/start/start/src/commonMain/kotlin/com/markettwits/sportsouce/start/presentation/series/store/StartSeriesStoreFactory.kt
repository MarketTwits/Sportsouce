package com.markettwits.sportsouce.start.presentation.series.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.sportsouce.start.presentation.series.store.StartSeriesStore.State
import com.markettwits.sportsouce.starts.common.domain.StartsListItem

class StartSeriesStoreFactory(
    private val storeFactory: StoreFactory,
) {
    fun create(
        items: List<StartsListItem>,
        currentStartId: Int,
    ): StartSeriesStore =
        object : StartSeriesStore, Store<StartSeriesStore.Intent, State, StartSeriesStore.Label> by storeFactory.create(
            name = "StartSeriesStore",
            initialState = State(
                items = items,
                currentStartId = currentStartId,
            ),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { StartSeriesExecutor() },
            reducer = StartSeriesReducer
        ) {}
}
