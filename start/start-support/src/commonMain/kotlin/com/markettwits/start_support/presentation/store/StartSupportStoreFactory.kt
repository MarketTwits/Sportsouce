package com.markettwits.start_support.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.IntentAction
import com.markettwits.start_support.domain.StartSupportUseCase
import com.markettwits.start_support.presentation.store.StartSupportStore.Intent
import com.markettwits.start_support.presentation.store.StartSupportStore.Label
import com.markettwits.start_support.presentation.store.StartSupportStore.State

class StartSupportStoreFactory(
    private val storeFactory: StoreFactory,
    private val useCase: StartSupportUseCase,
    private val intentAction: IntentAction,
) {

    fun create(startId: Int): StartSupportStore =
        StartSupportStoreImpl(useCase, intentAction, startId)

    private inner class StartSupportStoreImpl(
        private val useCase: StartSupportUseCase,
        private val intentAction: IntentAction,
        private val startId: Int
    ) : StartSupportStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "StartSupportStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { StartSupportExecutor(intentAction, useCase, startId) },
            reducer = StartSupportReducer
        )
}