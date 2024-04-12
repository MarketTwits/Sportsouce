package com.markettwits.selfupdater.components.selft_update.store.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.flipperdevices.selfupdater.api.SelfUpdaterApi
import com.markettwits.selfupdater.components.notification.model.NewAppVersion
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Intent
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Label
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.State

class SelfUpdateStoreFactory(
    private val storeFactory: StoreFactory,
    private val selfUpdaterApi: SelfUpdaterApi
) {

    fun create(newAppVersion: NewAppVersion?): SelfUpdateStore = SelfUpdateStoreImpl(newAppVersion)

    private inner class SelfUpdateStoreImpl(private val newAppVersion: NewAppVersion?) :
        SelfUpdateStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "SelfUpdateStore",
            initialState = State(newAppInfo = newAppVersion),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { SelfUpdateExecutor(selfUpdaterApi) },
            reducer = SelfUpdateReducer
        )
}