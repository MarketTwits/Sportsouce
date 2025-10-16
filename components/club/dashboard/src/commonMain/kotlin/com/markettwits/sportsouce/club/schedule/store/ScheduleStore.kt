package com.markettwits.sportsouce.club.schedule.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.sportsouce.club.info.domain.models.Schedule
import com.markettwits.sportsouce.club.registration.domain.RegistrationType

interface ScheduleStore : Store<ScheduleStore.Intent, ScheduleStore.State, ScheduleStore.Label> {

    data class State(
        val schedules: List<Schedule> = emptyList(),
        val selectedSchedule: Schedule? = null,
        val selectedWorkoutId: Int? = null,
        val workoutTypes: List<String> = emptyList(),
        val allWorkoutTypes: List<String> = emptyList(),
        val isLoading: Boolean = false,
        val error: SauceError? = null,
    )

    sealed interface Intent {
        data object OnClickBack : Intent
        data class OnClickScheduleItem(val scheduleId: String, val scheduleName: String) : Intent
        data class OnClickWorkoutType(val workoutId: Int?) : Intent
        data object RetryRequest : Intent
    }

    sealed interface Message {
        data object Loading : Message
        data class Failed(val error: SauceError) : Message
        data class Loaded(val schedules: List<Schedule>, val workoutTypes: List<String>) : Message
        data class LoadedAllWorkoutTypes(val allWorkoutTypes: List<String>) : Message
        data class UpdateSelectedSchedule(val schedule: Schedule?) : Message
        data class UpdateSelectedWorkoutType(val workoutId: Int?) : Message
    }

    sealed interface Label {
        data object Dismiss : Label
        data class Registration(val type: RegistrationType) : Label
    }
}