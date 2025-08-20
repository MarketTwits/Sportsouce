package com.markettwits.sportsouce.start.support.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.IntentAction
import com.markettwits.sportsouce.start.support.domain.StartSupportUseCase
import com.markettwits.sportsouce.start.support.presentation.store.StartSupportStore.*

class StartSupportStoreFactory(
    private val storeFactory: StoreFactory,
    private val useCase: StartSupportUseCase,
    private val intentAction: IntentAction,
) {

    fun create(): StartSupportStore =
        StartSupportStoreImpl(useCase, intentAction)

    private inner class StartSupportStoreImpl(
        private val useCase: StartSupportUseCase,
        private val intentAction: IntentAction,
    ) : StartSupportStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "StartSupportStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { StartSupportExecutor(intentAction, useCase) },
            reducer = StartSupportReducer
        )
}