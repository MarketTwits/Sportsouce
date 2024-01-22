package com.markettwits.profile.presentation.sign_in

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class MockSignInScreenComponent : SignInScreen {
    override val state: Value<SignInUiState> = MutableValue(SignInUiState.Loading)
    override val fieldState: Value<SignInFieldUiState> = MutableValue(SignInFieldUiState("","",false))
    override fun signUp() {

    }

    override fun logIn() {

    }

    override fun messageHasBeenShowed() {

    }

    override fun handleEmail(email: String) {

    }

    override fun handlePassword(password: String) {

    }


}