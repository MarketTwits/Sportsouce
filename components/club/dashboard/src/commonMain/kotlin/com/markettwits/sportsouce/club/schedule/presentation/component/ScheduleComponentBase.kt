package com.markettwits.sportsouce.club.schedule.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.sportsouce.bottom_bar.component.listener.BottomBarComponentHandler
import com.markettwits.sportsouce.bottom_bar.component.listener.BottomBarVisibilityStrategy
import com.markettwits.sportsouce.club.common.domain.ClubRepository
import com.markettwits.sportsouce.club.schedule.presentation.store.ScheduleStore
import com.markettwits.sportsouce.club.schedule.presentation.store.ScheduleStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ScheduleComponentBase(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val output: (ScheduleComponent.Output) -> Unit,
    private val repository: ClubRepository,
) : ScheduleComponent, BottomBarComponentHandler(), ComponentContext by componentContext {

    init {
        subscribeOnBottomBar(BottomBarVisibilityStrategy.AlwaysInvisible)
    }

    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    private val store = instanceKeeper.getStore {
        ScheduleStoreFactory(
            storeFactory = storeFactory,
            repository = repository
        ).create()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<ScheduleStore.State> = store.stateFlow

    override fun obtainEvent(intent: ScheduleStore.Intent) {
        store.accept(intent)
    }

    init {
        store.labels.onEach { label ->
            when (label) {
                is ScheduleStore.Label.Dismiss -> {
                    output(ScheduleComponent.Output.Dismiss)
                }

                is ScheduleStore.Label.Registration -> {
                    output(ScheduleComponent.Output.Registration(label.type))
                }
            }
        }.launchIn(scope)
    }
}