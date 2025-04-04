package com.markettwits.sportsouce.club.registration.presentation.component

import com.markettwits.sportsouce.club.registration.domain.WorkoutRegistrationForm
import com.markettwits.sportsouce.club.registration.presentation.store.store.WorkoutRegistrationStore
import kotlinx.coroutines.flow.StateFlow

interface WorkoutRegistrationComponent {
    val state: StateFlow<WorkoutRegistrationStore.State>
    fun onFormChanged(registrationForm: WorkoutRegistrationForm)
    fun onClickRegistration()
    fun onClickPhone(phone: String)
    fun onClickLink(link: String)
    fun onClickDismiss()
    fun onClickFinish()

    interface Output {
        data object Dismiss : Output
    }
}
