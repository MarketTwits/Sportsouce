package com.markettwits.selfupdater.components.selft_update.component

import com.markettwits.sportsouce.bottom_bar.component.listener.BottomBarComponentHandler
import com.markettwits.sportsouce.bottom_bar.component.listener.BottomBarVisibilityStrategy
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.selfupdater.components.notification.model.NewAppVersion
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SelfUpdateComponentBase(
    componentContext: ComponentContext,
    private val storeFactory: SelfUpdateStoreFactory,
    private val newAppVersion: NewAppVersion?,
    private val goBack: () -> Unit
) : BottomBarComponentHandler(), SelfUpdateComponent, ComponentContext by componentContext {

    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    private val store = instanceKeeper.getStore {
        storeFactory.create(newAppVersion)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<SelfUpdateStore.State> = store.stateFlow

    override fun obtainEvent(intent: SelfUpdateStore.Intent) {
        store.accept(intent)
    }

    init {
        subscribeOnBottomBar(BottomBarVisibilityStrategy.AlwaysInvisible)
        lifecycle.subscribe(object : Lifecycle.Callbacks {
            override fun onResume() { store.accept(SelfUpdateStore.Intent.ScreenResumed) }
            override fun onPause() { store.accept(SelfUpdateStore.Intent.ScreenPaused) }
            override fun onDestroy() { scope.cancel() }
        })
        store.labels.onEach {
            when (it) {
                SelfUpdateStore.Label.GoBack -> goBack()
            }
        }.launchIn(scope)
    }

}
