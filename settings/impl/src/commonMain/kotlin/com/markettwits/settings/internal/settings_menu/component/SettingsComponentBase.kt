package com.markettwits.settings.internal.settings_menu.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.settings.internal.settings_menu.store.SettingsStore
import com.markettwits.settings.internal.settings_menu.store.SettingsStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SettingsComponentBase(
    componentContext: ComponentContext,
    private val storeFactory: SettingsStoreFactory,
    private val pop: () -> Unit,
    private val output: (SettingsOutput) -> Unit
) : SettingsComponent, ComponentContext by componentContext {

    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }
    override val state: StateFlow<SettingsStore.State> = store.stateFlow
    override fun obtainEvent(intent: SettingsStore.Intent) {
        store.accept(intent)
    }

    init {
        store.labels.onEach {
            when (it) {
                is SettingsStore.Label.GoBack -> pop()
                is SettingsStore.Label.OutPut -> output(it.outPut)
            }
        }.launchIn(scope)
    }
}
