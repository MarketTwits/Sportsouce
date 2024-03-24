package com.markettwits.schedule.schedule.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.schedule.schedule.presentation.store.StartsScheduleStore
import com.markettwits.schedule.schedule.presentation.store.StartsScheduleStoreFactory
import com.markettwits.starts_common.domain.StartsListItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class StartsScheduleComponentBase(
    context: ComponentContext,
    private val storeFactory: StartsScheduleStoreFactory,
    private val onClickItem: (List<StartsListItem>) -> Unit,
    private val pop: () -> Unit
) : StartsScheduleComponent, ComponentContext by context {
    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }
    private val scope = CoroutineScope(Dispatchers.Main)
    override val value: StateFlow<StartsScheduleStore.State> = store.stateFlow
    override fun obtainEvent(event: StartsScheduleStore.Intent) {
        store.accept(event)
    }

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is StartsScheduleStore.Label.OnClickItem -> onClickItem(it.item)
                    is StartsScheduleStore.Label.Back -> pop()
                }
            }
        }
    }
}