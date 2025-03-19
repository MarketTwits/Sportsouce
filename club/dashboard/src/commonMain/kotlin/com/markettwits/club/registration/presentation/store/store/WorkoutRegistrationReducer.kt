package com.markettwits.club.registration.presentation.store.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.club.registration.presentation.store.store.WorkoutRegistrationStore.Message
import com.markettwits.club.registration.presentation.store.store.WorkoutRegistrationStore.State

object WorkoutRegistrationReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.RegistrationLoading -> copy(isLoading = true)
            is Message.OnValueChanged -> copy(form = msg.workoutRegistrationForm)
            is Message.RegistrationFailed -> copy(
                isError = true,
                isLoading = false,
                message = msg.message
            )
            is Message.RegistrationSuccess -> copy(
                isError = false,
                isLoading = false,
                isSuccess = true
            )
            is Message.RegistrationReset -> copy(isSuccess = false)
        }
    }
}