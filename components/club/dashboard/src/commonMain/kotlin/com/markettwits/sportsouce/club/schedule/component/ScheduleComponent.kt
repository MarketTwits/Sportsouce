package com.markettwits.sportsouce.club.schedule.component

import com.markettwits.sportsouce.club.registration.domain.RegistrationType
import com.markettwits.sportsouce.club.schedule.store.ScheduleStore
import kotlinx.coroutines.flow.StateFlow

interface ScheduleComponent {
    val state: StateFlow<ScheduleStore.State>
    fun obtainEvent(intent: ScheduleStore.Intent)

    sealed interface Output {
        data object Dismiss : Output
        data class Registration(val type: RegistrationType) : Output
    }
}