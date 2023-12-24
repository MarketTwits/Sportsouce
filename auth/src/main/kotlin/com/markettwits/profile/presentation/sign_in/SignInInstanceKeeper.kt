package com.markettwits.profile.presentation.sign_in

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.markettwits.profile.data.AuthDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignInInstanceKeeper(
    private val service: AuthDataSource,
    private val toProfile : () -> Unit
) : InstanceKeeper.Instance {
    private val scope = CoroutineScope(Dispatchers.IO)
    val authUiState: MutableValue<SignInUiState> = MutableValue(SignInUiState.Initial)
    val fieldState: MutableValue<SignInFieldUiState> =
        MutableValue(SignInFieldUiState("", "", false))

    fun login() {
        scope.launch {
            authUiState.value = SignInUiState.Loading
            authUiState.value = service.logIn(fieldState.value.email, fieldState.value.password)
            if (authUiState.value is SignInUiState.Success){
                withContext(Dispatchers.Main){
                    toProfile()
                }
            }
        }
    }

    fun validate() {
        if (fieldState.value.email.isNotEmpty() && fieldState.value.password.isNotEmpty()) {
            fieldState.value = fieldState.value.copy(enabled = true)
        } else {
            fieldState.value = fieldState.value.copy(enabled = false)
        }
    }
}