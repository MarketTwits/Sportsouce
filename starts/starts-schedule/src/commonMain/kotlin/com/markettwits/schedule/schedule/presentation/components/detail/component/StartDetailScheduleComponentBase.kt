package com.markettwits.schedule.schedule.presentation.components.detail.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.schedule.schedule.presentation.components.detail.store.StartDetailScheduleStore
import com.markettwits.schedule.schedule.presentation.components.detail.store.StartDetailScheduleStoreFactory
import com.markettwits.starts_common.domain.StartsListItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StartDetailScheduleComponentBase(
    componentContext: ComponentContext,
    private val storeFactory: StartDetailScheduleStoreFactory,
    private val back: () -> Unit,
    private val onClickStart: (Int) -> Unit,
    private val start: List<StartsListItem>
) :
    StartDetailScheduleComponent, ComponentContext by componentContext {
    private val scope = CoroutineScope(Dispatchers.Main.immediate)
    private val store = instanceKeeper.getStore {
        storeFactory.create(start)
    }
    override val state: StateFlow<StartDetailScheduleStore.State> = store.stateFlow
    override fun obtainEvent(intent: StartDetailScheduleStore.Intent) {
        store.accept(intent)
    }

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is StartDetailScheduleStore.Label.OnClickBack -> back()
                    is StartDetailScheduleStore.Label.OnClickItem -> onClickStart(it.id)
                }
            }
        }
    }
}
