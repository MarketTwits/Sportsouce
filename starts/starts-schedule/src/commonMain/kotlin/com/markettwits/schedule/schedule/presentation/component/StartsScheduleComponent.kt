package com.markettwits.schedule.schedule.presentation.component

import com.markettwits.schedule.schedule.presentation.store.StartsScheduleStore
import kotlinx.coroutines.flow.StateFlow

interface StartsScheduleComponent {
    val value: StateFlow<StartsScheduleStore.State>
    fun obtainEvent(event: StartsScheduleStore.Intent)
}