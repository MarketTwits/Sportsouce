package com.markettwits.selfupdater.components.notification.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.selfupdater.api.SelfUpdaterApi
import com.markettwits.sportsouce.inappnotification.api.InAppNotificationStorage
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStore.Intent
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStore.Label
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStore.State
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.TimeSource

class InAppNotificationStoreFactory(
    private val storeFactory: StoreFactory,
    private val selfUpdaterApi: SelfUpdaterApi,
    private val notificationStorage: InAppNotificationStorage,
    private val timeSource: TimeSource = TimeSource.Monotonic,
    private val checkInterval: Duration = 24.hours,
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
            executorFactory = { InAppNotificationExecutor(selfUpdaterApi, notificationStorage, timeSource, checkInterval) },
            reducer = InAppNotificationReducer
        )
}
