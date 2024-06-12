package com.markettwits.club.registration.presentation.store.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.club.registration.domain.RegistrationType
import com.markettwits.club.registration.domain.WorkoutRegistrationForm
import com.markettwits.club.registration.presentation.store.store.WorkoutRegistrationStore.Intent
import com.markettwits.club.registration.presentation.store.store.WorkoutRegistrationStore.Label
import com.markettwits.club.registration.presentation.store.store.WorkoutRegistrationStore.State

interface WorkoutRegistrationStore : Store<Intent, State, Label> {

    data class State(
        val form: WorkoutRegistrationForm = WorkoutRegistrationForm(
            RegistrationType.Empty,
            ",",
            "",
            ""
        ),
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val isSuccess: Boolean = false,
        val message: String = "",
    )

    sealed interface Intent {
        data object Dismiss : Intent
        data object OnClickContinueAfterSuccess : Intent
        data class OnValueChanged(val workoutRegistrationForm: WorkoutRegistrationForm) : Intent
        data object OnClickRegistration : Intent
        data class OnClickPhone(val phone: String) : Intent
        data class OnClickUrl(val url: String) : Intent
    }

    sealed interface Message {
        data class OnValueChanged(val workoutRegistrationForm: WorkoutRegistrationForm) : Message
        data object RegistrationLoading : Message
        data object RegistrationSuccess : Message
        data class RegistrationFailed(val message: String) : Message
        data object RegistrationReset : Message
    }

    sealed interface Label {
        data object Dismiss : Label
    }

}
