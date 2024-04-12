package com.markettwits.settings.internal.change_theme.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory

import com.markettwits.settings.api.api.MutableSettingsRepository

class ChangeThemeStoreFactory(
    private val storeFactory: StoreFactory,
    private val settingsRepository: MutableSettingsRepository
) {

    fun create(): ChangeThemeStore = ChangeThemeStoreImpl(settingsRepository)

    private inner class ChangeThemeStoreImpl(
        private val settingsRepository: MutableSettingsRepository
    ) : ChangeThemeStore,
        Store<ChangeThemeStore.Intent, ChangeThemeStore.State, ChangeThemeStore.Label> by storeFactory.create(
            name = "ChangeThemeStore",
            initialState = ChangeThemeStore.State(emptyList()),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { ChangeThemeExecutor(settingsRepository) },
            reducer = ChangeThemeReducer
        )
}