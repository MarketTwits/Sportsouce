package com.markettwits.sportsouce.club.registration.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.IntentAction
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.core.errors.api.throwable.mapToString
import com.markettwits.sportsouce.club.registration.domain.RegistrationType
import com.markettwits.sportsouce.club.registration.domain.WorkoutRegistrationForm
import com.markettwits.sportsouce.club.registration.domain.WorkoutRegistrationUseCase
import kotlinx.coroutines.launch

class WorkoutRegistrationExecutor(
    private val workoutRegistrationUseCase: WorkoutRegistrationUseCase,
    private val intentAction: IntentAction,
    private val registrationType: RegistrationType,
) : CoroutineExecutor<WorkoutRegistrationStore.Intent, Unit, WorkoutRegistrationStore.State, WorkoutRegistrationStore.Message, WorkoutRegistrationStore.Label>() {
    override fun executeIntent(intent: WorkoutRegistrationStore.Intent) {
        when (intent) {
            is WorkoutRegistrationStore.Intent.Dismiss -> publish(WorkoutRegistrationStore.Label.Dismiss)
            is WorkoutRegistrationStore.Intent.OnValueChanged -> dispatch(
                WorkoutRegistrationStore.Message.OnValueChanged(
                    intent.workoutRegistrationForm
                )
            )

            is WorkoutRegistrationStore.Intent.OnClickRegistration -> {
                onClickRegistration(state().form)
            }

            is WorkoutRegistrationStore.Intent.OnClickPhone -> intentAction.openPhone(intent.phone)
            is WorkoutRegistrationStore.Intent.OnClickUrl -> intentAction.openWebPage(intent.url)
            is WorkoutRegistrationStore.Intent.OnClickContinueAfterSuccess -> {
                dispatch(WorkoutRegistrationStore.Message.RegistrationReset)
                publish(WorkoutRegistrationStore.Label.Dismiss)
            }
        }
    }

    override fun executeAction(action: Unit) {
        scope.launch {
            val value = workoutRegistrationUseCase.init()
            println("WorkoutRegistrationExecutor $value")
            dispatch(WorkoutRegistrationStore.Message.OnValueChanged(value.copy(type = registrationType)))
        }
    }

    private fun onClickRegistration(workoutRegistrationForm: WorkoutRegistrationForm) {
        scope.launch {
            dispatch(WorkoutRegistrationStore.Message.RegistrationLoading)
            workoutRegistrationUseCase.register(workoutRegistrationForm)
                .fold(onSuccess = {
                    dispatch(WorkoutRegistrationStore.Message.RegistrationSuccess)
                }, onFailure = {
                    dispatch(WorkoutRegistrationStore.Message.RegistrationFailed(it.mapToSauceError().mapToString()))
                })
        }
    }
}

