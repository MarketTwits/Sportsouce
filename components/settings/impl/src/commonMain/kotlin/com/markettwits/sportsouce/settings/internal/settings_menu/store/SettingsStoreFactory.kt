package com.markettwits.sportsouce.settings.internal.settings_menu.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.IntentAction
import com.markettwits.cahce.CacheManagerImpl
import com.markettwits.sportsouce.settings.internal.settings_menu.store.SettingsStore.*
import com.markettwits.version.ApplicationVersionManager

class SettingsStoreFactory(
    private val storeFactory: StoreFactory,
    private val versionManager: ApplicationVersionManager,
    private val intentAction: IntentAction
) {

    fun create(showNotificationSettings: Boolean): SettingsStore =
        SettingsStoreImpl(intentAction, versionManager, showNotificationSettings)


    private inner class SettingsStoreImpl(
        private val intentAction: IntentAction,
        private val versionManager: ApplicationVersionManager,
        showNotificationSettings: Boolean,
    ) : SettingsStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "SettingsStore",
            initialState = State(null, showNotificationSettings = showNotificationSettings),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { SettingsExecutor(intentAction, versionManager, CacheManagerImpl()) },
            reducer = SettingsReducer
        )
}
