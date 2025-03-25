package com.markettwits.sportsouce.profile.registrations.detail.store.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.IntentAction
import com.markettwits.sportsouce.profile.registrations.detail.store.store.StartOrderStore.Intent
import com.markettwits.sportsouce.profile.registrations.detail.store.store.StartOrderStore.Label
import com.markettwits.sportsouce.profile.registrations.detail.store.store.StartOrderStore.State
import com.markettwits.sportsouce.profile.registrations.list.data.StartOrderRegistrationRepository
import com.markettwits.sportsouce.profile.registrations.list.domain.StartOrderInfo

class StartOrderStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: StartOrderRegistrationRepository,
    private val intentAction: IntentAction
) {
    fun create(start: StartOrderInfo): StartOrderStore =
        StartOrderStoreImpl(start, repository, intentAction)

    private inner class StartOrderStoreImpl(
        private val start: StartOrderInfo,
        private val repository: StartOrderRegistrationRepository,
        private val intentAction: IntentAction
    ) :
        StartOrderStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "StartOrderStore",
            initialState = State(startOrderInfo = start),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { StartOrderStoreExecutor(repository, intentAction) },
            reducer = StartOrderStoreReducer
        )
}