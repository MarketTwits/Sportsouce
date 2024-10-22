package com.markettwits.club.registration.presentation.store.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.IntentAction
import com.markettwits.club.registration.domain.WorkoutRegistrationForm
import com.markettwits.club.registration.domain.WorkoutRegistrationUseCase
import com.markettwits.club.registration.presentation.store.store.WorkoutRegistrationStore.Intent
import com.markettwits.club.registration.presentation.store.store.WorkoutRegistrationStore.Label
import com.markettwits.club.registration.presentation.store.store.WorkoutRegistrationStore.Message
import com.markettwits.club.registration.presentation.store.store.WorkoutRegistrationStore.State
import com.markettwits.core.errors.api.throwable.networkExceptionHandler
import kotlinx.coroutines.launch

class WorkoutRegistrationExecutor(
    private val workoutRegistrationUseCase: WorkoutRegistrationUseCase,
    private val intentAction: IntentAction
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.Dismiss -> publish(Label.Dismiss)
            is Intent.OnValueChanged -> dispatch(Message.OnValueChanged(intent.workoutRegistrationForm))
            is Intent.OnClickRegistration -> {
                onClickRegistration(getState().form)
            }
            is Intent.OnClickPhone -> intentAction.openPhone(intent.phone)
            is Intent.OnClickUrl -> intentAction.openWebPage(intent.url)
            is Intent.OnClickContinueAfterSuccess -> {
                dispatch(Message.RegistrationReset)
                publish(Label.Dismiss)
            }
        }
    }

    private fun onClickRegistration(workoutRegistrationForm: WorkoutRegistrationForm) {
        scope.launch {
            dispatch(Message.RegistrationLoading)
            workoutRegistrationUseCase.register(workoutRegistrationForm)
                .fold(onSuccess = {
                    dispatch(Message.RegistrationSuccess)
                }, onFailure = {
                    dispatch(Message.RegistrationFailed(it.networkExceptionHandler().message.toString()))
                })
        }
    }
}

