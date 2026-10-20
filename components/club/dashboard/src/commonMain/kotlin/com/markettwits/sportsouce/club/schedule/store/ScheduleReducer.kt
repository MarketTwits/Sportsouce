package com.markettwits.sportsouce.club.schedule.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.sportsouce.club.schedule.store.ScheduleStore.*

object ScheduleReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State = when (msg) {
        Message.Loading -> copy(isLoading = true, error = null)
        is Message.Failed -> copy(isLoading = false, error = msg.error)
        is Message.Loaded -> {
            val selected = selectedWorkoutId?.takeIf { id -> msg.schedules.any { it.workoutId == id } }
            copy(isLoading = false, error = null, allSchedules = msg.schedules, selectedWorkoutId = selected,
                schedules = msg.schedules.filter { selected == null || it.workoutId == selected })
        }
        is Message.UpdateSelectedSchedule -> copy(selectedSchedule = msg.schedule)
        is Message.UpdateSelectedWorkoutType -> copy(selectedWorkoutId = msg.workoutId,
            schedules = allSchedules.filter { msg.workoutId == null || it.workoutId == msg.workoutId })
    }
}
