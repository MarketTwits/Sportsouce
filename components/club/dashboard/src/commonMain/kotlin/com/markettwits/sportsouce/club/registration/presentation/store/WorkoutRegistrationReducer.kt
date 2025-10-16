package com.markettwits.sportsouce.club.registration.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer

object WorkoutRegistrationReducer : Reducer<WorkoutRegistrationStore.State, WorkoutRegistrationStore.Message> {
    override fun WorkoutRegistrationStore.State.reduce(msg: WorkoutRegistrationStore.Message): WorkoutRegistrationStore.State {
        return when (msg) {
            is WorkoutRegistrationStore.Message.RegistrationLoading -> copy(isLoading = true)
            is WorkoutRegistrationStore.Message.OnValueChanged -> copy(form = msg.workoutRegistrationForm)
            is WorkoutRegistrationStore.Message.RegistrationFailed -> copy(
                isError = true,
                isLoading = false,
                message = msg.message
            )

            is WorkoutRegistrationStore.Message.RegistrationSuccess -> copy(
                isError = false,
                isLoading = false,
                isSuccess = true
            )

            is WorkoutRegistrationStore.Message.RegistrationReset -> copy(isSuccess = false)
        }
    }
}