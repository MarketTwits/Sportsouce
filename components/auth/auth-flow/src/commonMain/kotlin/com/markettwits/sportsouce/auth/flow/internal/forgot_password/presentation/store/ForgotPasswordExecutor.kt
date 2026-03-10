package com.markettwits.sportsouce.auth.flow.internal.forgot_password.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.core.errors.api.throwable.mapToString
import com.markettwits.sportsouce.auth.flow.internal.forgot_password.domain.use_case.ForgotPasswordUseCase
import com.markettwits.sportsouce.auth.flow.internal.forgot_password.presentation.store.ForgotPasswordStore.*
import kotlinx.coroutines.launch

internal class ForgotPasswordExecutor(private val useCase: ForgotPasswordUseCase) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.OnConsumedEvent -> dispatch(Message.OnConsumedEvent)
            is Intent.OnValueChange -> dispatch(Message.OnValueChanged(intent.email))
            is Intent.OnClickReset -> launch(state().email)
            is Intent.OnClickBack -> publish(Label.OnClickBack)
        }
    }

    private fun launch(email: String) {
        scope.launch {
            dispatch(Message.Loading)
            useCase.resetPassword(email).fold(
                onSuccess = {
                    dispatch(Message.Success(it))
                }, onFailure = {
                    dispatch(Message.Failed(it.mapToSauceError().mapToString()))
                }
            )
        }
    }
}
