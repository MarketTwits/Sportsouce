package com.markettwits.profile.internal.forgot_password.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.cloud.model.auth.common.ErrorResponse
import com.markettwits.profile.internal.forgot_password.domain.use_case.ForgotPasswordUseCase
import com.markettwits.profile.internal.forgot_password.presentation.store.ForgotPasswordStore.Intent
import com.markettwits.profile.internal.forgot_password.presentation.store.ForgotPasswordStore.Label
import com.markettwits.profile.internal.forgot_password.presentation.store.ForgotPasswordStore.Message
import com.markettwits.profile.internal.forgot_password.presentation.store.ForgotPasswordStore.State
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
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
                    val message = when (it) {
                        is ClientRequestException -> it.response.body<ErrorResponse>().message
                        else -> it.message.toString()
                    }
                    dispatch(Message.Failed(message))
                }
            )
        }
    }
}
