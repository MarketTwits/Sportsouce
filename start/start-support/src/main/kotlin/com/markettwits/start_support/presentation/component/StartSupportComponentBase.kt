package com.markettwits.start_support.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.start_support.presentation.store.StartSupportStore
import com.markettwits.start_support.presentation.store.StartSupportStoreFactory
import kotlinx.coroutines.flow.StateFlow

class StartSupportComponentBase(
    componentContext: ComponentContext,
    private val storeFactory: StartSupportStoreFactory,
    private val startId: Int
) : StartSupportComponent,
    ComponentContext by componentContext {
    private val store = instanceKeeper.getStore {
        storeFactory.create(startId)
    }
    override val state: StateFlow<StartSupportStore.State>
        get() = store.stateFlow

    override fun obtainEvent(intent: StartSupportStore.Intent) {
        store.accept(intent)
    }
}
