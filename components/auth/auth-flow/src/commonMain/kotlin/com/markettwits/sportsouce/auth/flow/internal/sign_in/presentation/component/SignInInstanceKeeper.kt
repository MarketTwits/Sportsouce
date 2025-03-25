package com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.component

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.markettwits.core.errors.api.throwable.networkExceptionHandler
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.sportsouce.auth.flow.internal.sign_in.domain.SignInUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInInstanceKeeper(
    private val useCase: SignInUseCase,
    private val exceptionTracker: ExceptionTracker,
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
                    exceptionTracker.setUserId(it.id.toString())
                    scope.launch {
                        labels.value = SignInOutPuts.GoProfile
                    }
                }, onFailure = {
                    val message = it.networkExceptionHandler().message.toString()
                    exceptionTracker.setLog("email : ${fieldState.value.email}\npassword: ${fieldState.value.password}")
                    exceptionTracker.reportException(it, key = "#SignInInstanceKeeper#login")
                    authUiState.value = SignInUiState.Error(message = message, messageShow = false)
                })
        }
    }

    fun validate() {
        if (validatePhone(fieldState.value.email) && fieldState.value.password.isNotEmpty()) {
            fieldState.value = fieldState.value.copy(enabled = true)
        } else {
            fieldState.value = fieldState.value.copy(enabled = false)
        }
    }

    private fun validatePhone(phone: String): Boolean {
        val regex = Regex("""\+7 \(\d{3}\) \d{3}-\d{2}-\d{2}""")
        return regex.matches(phone)
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