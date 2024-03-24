package com.markettwits.schedule.schedule.presentation.components.detail.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.schedule.schedule.presentation.components.detail.store.StartDetailScheduleStore.Intent
import com.markettwits.schedule.schedule.presentation.components.detail.store.StartDetailScheduleStore.Label
import com.markettwits.schedule.schedule.presentation.components.detail.store.StartDetailScheduleStore.State
import com.markettwits.starts_common.domain.StartsListItem

class StartDetailScheduleStoreFactory(
    private val storeFactory: StoreFactory,
) {

    fun create(start: List<StartsListItem>): StartDetailScheduleStore =
        StartDetailScheduleStoreImpl(start)

    private inner class StartDetailScheduleStoreImpl(
        private val start: List<StartsListItem>
    ) :
        StartDetailScheduleStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "StartDetailScheduleStore",
            initialState = State(start),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { StartDetailScheduleExecutor() },
            reducer = StartDetailScheduleReducer
        )
}