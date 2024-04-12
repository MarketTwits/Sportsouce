package com.markettwits.settings.internal.settings_menu.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.IntentAction
import com.markettwits.settings.internal.settings_menu.store.SettingsStore.Intent
import com.markettwits.settings.internal.settings_menu.store.SettingsStore.Label
import com.markettwits.settings.internal.settings_menu.store.SettingsStore.State

class SettingsStoreFactory(
    private val storeFactory: StoreFactory,
    private val intentAction: IntentAction
) {

    fun create(): SettingsStore = SettingsStoreImpl(intentAction)


    private inner class SettingsStoreImpl(
        private val intentAction: IntentAction
    ) : SettingsStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "SettingsStore",
            initialState = State,
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { SettingsExecutor(intentAction) },
            reducer = SettingsReducer
        )
}