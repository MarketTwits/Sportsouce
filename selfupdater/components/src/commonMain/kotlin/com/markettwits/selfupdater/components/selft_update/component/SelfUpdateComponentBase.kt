package com.markettwits.selfupdater.components.selft_update.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.selfupdater.components.notification.model.NewAppVersion
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SelfUpdateComponentBase(
    componentContext: ComponentContext,
    private val storeFactory: SelfUpdateStoreFactory,
    private val newAppVersion: NewAppVersion?,
    private val goBack: () -> Unit
) : SelfUpdateComponent, ComponentContext by componentContext {
    private val scope = CoroutineScope(Dispatchers.Main.immediate)
    private val store = instanceKeeper.getStore {
        storeFactory.create(newAppVersion)
    }
    override val state: StateFlow<SelfUpdateStore.State> = store.stateFlow
    override fun obtainEvent(intent: SelfUpdateStore.Intent) {
        store.accept(intent)
    }

    init {
        store.labels.onEach {
            when (it) {
                SelfUpdateStore.Label.GoBack -> goBack()
            }
        }.launchIn(scope)
    }

}
