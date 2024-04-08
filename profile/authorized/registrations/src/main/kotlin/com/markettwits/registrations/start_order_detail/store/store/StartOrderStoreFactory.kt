package com.markettwits.registrations.start_order_detail.store.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.registrations.registrations_list.data.StartOrderRegistrationRepository
import com.markettwits.registrations.registrations_list.domain.StartOrderInfo
import com.markettwits.registrations.start_order_detail.store.store.StartOrderStore.Intent
import com.markettwits.registrations.start_order_detail.store.store.StartOrderStore.Label
import com.markettwits.registrations.start_order_detail.store.store.StartOrderStore.State

class StartOrderStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: StartOrderRegistrationRepository
) {
    fun create(start: StartOrderInfo): StartOrderStore = StartOrderStoreImpl(start, repository)

    private inner class StartOrderStoreImpl(
        private val start: StartOrderInfo,
        private val repository: StartOrderRegistrationRepository
    ) :
        StartOrderStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "StartOrderStore",
            initialState = State(startOrderInfo = start),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { StartOrderStoreExecutor(repository) },
            reducer = StartOrderStoreReducer
        )
}