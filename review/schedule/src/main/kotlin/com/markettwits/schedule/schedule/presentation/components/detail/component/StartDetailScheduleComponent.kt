package com.markettwits.schedule.schedule.presentation.components.detail.component

import com.markettwits.schedule.schedule.presentation.components.detail.store.StartDetailScheduleStore
import kotlinx.coroutines.flow.StateFlow

interface StartDetailScheduleComponent {
    val state: StateFlow<StartDetailScheduleStore.State>
    fun obtainEvent(intent: StartDetailScheduleStore.Intent)
}
