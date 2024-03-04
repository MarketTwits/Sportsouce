package com.markettwits.profile.internal.sign_in.presentation.component

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.markettwits.cloud.model.auth.common.AuthErrorResponse
import com.markettwits.profile.internal.sign_in.domain.SignInUseCase
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SignInInstanceKeeper(
    private val useCase: SignInUseCase,
) : InstanceKeeper.Instance {
    private val scope = CoroutineScope(Dispatchers.Main.immediate)
    val labels: MutableValue<SignInOutPuts> = MutableValue(SignInOutPuts.Empty)
    val authUiState: MutableValue<SignInUiState> = MutableValue(SignInUiState.Initial)
    val fieldState: MutableValue<SignInFieldUiState> =
        MutableValue(SignInFieldUiState("", "", false))

    fun login() {
        scope.launch {
            authUiState.value = SignInUiState.Loading
            useCase.signIn(fieldState.value.email, fieldState.value.password).fold(
                onSuccess = {
                    authUiState.value = SignInUiState.Success
                    scope.launch {
                        labels.value = SignInOutPuts.GoProfile
                    }
                }, onFailure = {
                    val message = when (it) {
                        is ClientRequestException -> it.response.body<AuthErrorResponse>().message
                        else -> it.message.toString()
                    }
                    authUiState.value = SignInUiState.Error(message = message, messageShow = true)
                })
        }
    }

    fun validate() {
        if (fieldState.value.email.isNotEmpty() && fieldState.value.password.isNotEmpty()) {
            fieldState.value = fieldState.value.copy(enabled = true)
        } else {
            fieldState.value = fieldState.value.copy(enabled = false)
        }
    }

    fun signUp() {
        scope.launch {
            labels.value = SignInOutPuts.GoSignUp
        }
    }
}

sealed interface SignInOutPuts {
    data object GoSignUp : SignInOutPuts
    data object GoProfile : SignInOutPuts
    data object Empty : SignInOutPuts
}