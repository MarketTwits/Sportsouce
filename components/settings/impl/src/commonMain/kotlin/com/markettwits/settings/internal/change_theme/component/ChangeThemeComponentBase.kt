package com.markettwits.settings.internal.change_theme.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.settings.internal.change_theme.store.ChangeThemeStore
import com.markettwits.settings.internal.change_theme.store.ChangeThemeStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ChangeThemeComponentBase(
    componentContext: ComponentContext,
    private val pop: () -> Unit,
    private val storeFactory: ChangeThemeStoreFactory
) : ChangeThemeComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }

    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<ChangeThemeStore.State> = store.stateFlow

    override fun obtainEvent(intent: ChangeThemeStore.Intent) {
        store.accept(intent)
    }

    init {
        store.labels.onEach {
            when (it) {
                is ChangeThemeStore.Label.Dismiss -> pop()
            }
        }.launchIn(scope)
    }

}