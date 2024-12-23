package com.markettwits.start.register.presentation.registration.presentation.component.pay.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.start.register.presentation.registration.presentation.component.pay.store.StartPayStore.Intent
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.markettwits.IntentAction
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.start.register.presentation.registration.domain.StartRegistrationRepository
import com.markettwits.start.register.presentation.registration.presentation.component.pay.store.StartPayStore.Label
import com.markettwits.start.register.presentation.registration.presentation.component.pay.store.StartPayStore.State
import com.markettwits.start.register.presentation.registration.presentation.component.pay.store.StartPayStore.Message
import com.markettwits.start.register.presentation.registration.presentation.components.registration.StartRegistrationStagePage

class StartPayStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: StartRegistrationRepository,
    private val intentAction: IntentAction,
    private val exceptionTracker: ExceptionTracker
) {

    fun create(startRegistrationStagePage: StartRegistrationStagePage.Pay): StartPayStore {
        return StartPayStoreImpl(startRegistrationStagePage)
    }

    private inner class StartPayStoreImpl(
        private val startRegistrationStagePage: StartRegistrationStagePage.Pay
    ) :
        StartPayStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "StartPayStore",
            initialState = State(true,startRegistrationStagePage),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { StartPayExecutor(repository, intentAction, exceptionTracker) },
            reducer = StartPayReducer
        )
}