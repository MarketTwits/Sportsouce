package com.markettwits.sportsouce.club.schedule.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.sportsouce.club.schedule.store.ScheduleStore.Message
import com.markettwits.sportsouce.club.schedule.store.ScheduleStore.State

object ScheduleReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State =
        when (msg) {
            is Message.Loading -> copy(
                isLoading = true,
                error = null
            )

            is Message.Failed -> copy(
                isLoading = false,
                error = msg.error
            )

            is Message.Loaded -> copy(
                isLoading = false,
                error = null,
                schedules = msg.schedules,
                workoutTypes = msg.workoutTypes
            )

            is Message.UpdateSelectedSchedule -> copy(
                selectedSchedule = msg.schedule
            )

            is Message.UpdateSelectedWorkoutType -> copy(
                selectedWorkoutId = msg.workoutId
            )

            is Message.LoadedAllWorkoutTypes -> copy(
                allWorkoutTypes = msg.allWorkoutTypes
            )
        }
}