package com.markettwits.sportsouce.club.schedule.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.sportsouce.club.common.domain.ClubRepository

class ScheduleStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: ClubRepository,
) {

    fun create(): ScheduleStore {
        return object : ScheduleStore,
            Store<ScheduleStore.Intent, ScheduleStore.State, ScheduleStore.Label> by storeFactory.create(
                name = "ScheduleStore",
                initialState = ScheduleStore.State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = { ScheduleExecutor(repository) },
                reducer = ScheduleReducer
            ) {}
    }

}