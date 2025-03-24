package com.markettwits.selfupdater.components.notification.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.flipperdevices.selfupdater.api.SelfUpdaterApi
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStore.Intent
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStore.Label
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStore.State

class InAppNotificationStoreFactory(
    private val storeFactory: StoreFactory,
    private val selfUpdaterApi: SelfUpdaterApi,
) {

    fun create(): InAppNotificationStore {
        return InAppNotificationStoreImpl(selfUpdaterApi)
    }

    private inner class InAppNotificationStoreImpl(
        private val selfUpdaterApi: SelfUpdaterApi
    ) :
        InAppNotificationStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "InAppNotificationStore",
            initialState = State.NotPresent,
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { InAppNotificationExecutor(selfUpdaterApi) },
            reducer = InAppNotificationReducer
        )
}