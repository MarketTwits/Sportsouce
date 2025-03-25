package com.markettwits.sportsouce.club.dashboard.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.sportsouce.bottom_bar.component.listener.BottomBarComponentHandler
import com.markettwits.sportsouce.bottom_bar.component.listener.BottomBarVisibilityStrategy
import com.markettwits.sportsouce.club.dashboard.presentation.store.ClubDashboardStore
import com.markettwits.sportsouce.club.dashboard.presentation.store.ClubDashboardStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class ClubDashboardComponentBase(
    private val componentContext: ComponentContext,
    private val storeFactory: ClubDashboardStoreFactory,
    private val output: (ClubDashboardComponent.Output) -> Unit,
) : ClubDashboardComponent, BottomBarComponentHandler(), ComponentContext by componentContext {

    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<ClubDashboardStore.State> = store.stateFlow

    override fun obtainEvent(intent: ClubDashboardStore.Intent) {
        store.accept(intent)
    }

    init {
        store.labels.onEach {
            when (it) {
                is ClubDashboardStore.Label.GoBack -> output(ClubDashboardComponent.Output.Dismiss)
                is ClubDashboardStore.Label.OnClickInfo -> output(
                    ClubDashboardComponent.Output.GoInfo(it.index, it.items)
                )

                is ClubDashboardStore.Label.OnClickRegistration -> output(
                    ClubDashboardComponent.Output.Subscription(
                        it.type
                    )
                )
            }
        }.launchIn(scope)
        subscribeOnBottomBar(BottomBarVisibilityStrategy.AlwaysInvisible)
    }
}