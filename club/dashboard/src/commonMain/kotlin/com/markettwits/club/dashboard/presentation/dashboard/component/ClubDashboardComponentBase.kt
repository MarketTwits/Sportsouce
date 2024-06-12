package com.markettwits.club.dashboard.presentation.dashboard.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.bottom_bar.component.component.BottomBarSideEffectHandlerAbstract
import com.markettwits.bottom_bar.component.storage.BottomBarStorage
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
    private val listener: BottomBarStorage,
    private val output: (ClubDashboardComponent.Output) -> Unit,
) : ClubDashboardComponent, BottomBarSideEffectHandlerAbstract(listener),
    ComponentContext by componentContext {

    private val scope = CoroutineScope(Dispatchers.Main.immediate)
    private val store = instanceKeeper.getStore {
        storeFactory.create(listener)
    }
    override val popInner = { output(ClubDashboardComponent.Output.Dismiss) }
    override val state: StateFlow<ClubDashboardStore.State> = store.stateFlow

    override fun obtainEvent(intent: ClubDashboardStore.Intent) {
        store.accept(intent)
    }

    init {
        store.labels.onEach {
            when (it) {
                is ClubDashboardStore.Label.GoBack -> goBackInner()
                is ClubDashboardStore.Label.OnClickInfo -> output(
                    ClubDashboardComponent.Output.GoInfo(
                        it.index,
                        it.items
                    )
                )
                is ClubDashboardStore.Label.OnClickRegistration -> output(
                    ClubDashboardComponent.Output.Subscription(
                        it.type
                    )
                )
            }
        }.launchIn(scope)
        backHandler.register(bottomBarBackHandler)
    }
}