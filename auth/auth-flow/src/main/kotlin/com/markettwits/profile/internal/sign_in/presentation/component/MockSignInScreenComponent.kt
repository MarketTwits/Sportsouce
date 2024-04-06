package com.markettwits.profile.internal.sign_in.presentation.component

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

internal class MockSignInScreenComponent : SignInScreen {
    override val state: Value<SignInUiState> = MutableValue(SignInUiState.Loading)
    override val fieldState: Value<SignInFieldUiState> = MutableValue(SignInFieldUiState("","",false))
    override fun signUp() {

    }

    override fun forgotPassword() {

    }

    override fun logIn() {

    }

    override fun messageHasBeenShowed() {

    }

    override fun handleEmail(email: String) {

    }

    override fun handlePhone(phone: String) {

    }

    override fun handlePassword(password: String) {

    }


}