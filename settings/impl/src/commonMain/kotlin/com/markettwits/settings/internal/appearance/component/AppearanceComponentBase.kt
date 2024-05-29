package com.markettwits.settings.internal.appearance.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.settings.internal.appearance.store.AppearanceStore
import com.markettwits.settings.internal.appearance.store.AppearanceStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class AppearanceComponentBase(
    componentContext: ComponentContext,
    private val pop: () -> Unit,
    private val storeFactory: AppearanceStoreFactory
) : AppearanceComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }

    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    override val state: StateFlow<AppearanceStore.State> = store.stateFlow

    override fun obtainEvent(intent: AppearanceStore.Intent) {
        store.accept(intent)
    }

    init {
        store.labels.onEach {
            when (it) {
                is AppearanceStore.Label.GoBack -> pop()
            }
        }.launchIn(scope)
    }

}