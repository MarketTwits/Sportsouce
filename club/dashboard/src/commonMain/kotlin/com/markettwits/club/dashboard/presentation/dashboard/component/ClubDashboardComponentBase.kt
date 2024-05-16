package com.markettwits.club.dashboard.presentation.dashboard.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.bottom_bar.component.BottomBarSideEffectHandlerAbstract
import com.markettwits.bottom_bar.component.listener.BottomBarVisibilityListener
import com.markettwits.club.dashboard.presentation.dashboard.store.ClubDashboardStore
import com.markettwits.club.dashboard.presentation.dashboard.store.ClubDashboardStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class ClubDashboardComponentBase(
    private val componentContext: ComponentContext,
    private val storeFactory: ClubDashboardStoreFactory,
    private val listener: BottomBarVisibilityListener,
    private val goBack: () -> Unit,
    private val goInfo: (Int) -> Unit
) : ClubDashboardComponent, BottomBarSideEffectHandlerAbstract(listener),
    ComponentContext by componentContext {

    private val scope = CoroutineScope(Dispatchers.Main.immediate)
    private val store = instanceKeeper.getStore {
        storeFactory.create(listener)
    }
    override val popInner = goBack
    override val state: StateFlow<ClubDashboardStore.State> = store.stateFlow

    override fun obtainEvent(intent: ClubDashboardStore.Intent) {
        store.accept(intent)
    }

    init {
        store.labels.onEach {
            when (it) {
                is ClubDashboardStore.Label.GoBack -> goBackInner()
                is ClubDashboardStore.Label.OnClickInfo -> goInfo(it.index)
            }
        }.launchIn(scope)
        backHandler.register(bottomBarBackHandler)
    }
}