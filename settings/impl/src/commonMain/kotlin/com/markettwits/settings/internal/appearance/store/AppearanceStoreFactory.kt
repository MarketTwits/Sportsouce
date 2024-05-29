package com.markettwits.settings.internal.appearance.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory

import com.markettwits.settings.api.api.MutableSettingsRepository

internal class AppearanceStoreFactory(
    private val storeFactory: StoreFactory,
    private val settingsRepository: MutableSettingsRepository
) {

    fun create(): AppearanceStore = AppearanceStoreImpl(settingsRepository)

    private inner class AppearanceStoreImpl(
        private val settingsRepository: MutableSettingsRepository
    ) : AppearanceStore,
        Store<AppearanceStore.Intent, AppearanceStore.State, AppearanceStore.Label> by storeFactory.create(
            name = "ChangeThemeStore",
            initialState = AppearanceStore.State(emptyList()),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { AppearanceExecutor(settingsRepository) },
            reducer = AppearanceReducer
        )
}