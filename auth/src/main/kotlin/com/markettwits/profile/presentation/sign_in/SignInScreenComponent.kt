package com.markettwits.profile.presentation.sign_in

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreateSimple


class SignInScreenComponent(
    private val context: ComponentContext,
    private val signInInstanceKeeper: SignInInstanceKeeper,
) : SignInScreen, ComponentContext by context {
    private val keeper = instanceKeeper.getOrCreateSimple { signInInstanceKeeper }
    override val state: Value<SignInUiState> = keeper.authUiState
    override val fieldState: Value<SignInFieldUiState> = keeper.fieldState

    override fun logIn() {
        keeper.login()
    }

    override fun messageHasBeenShowed() {
        keeper.authUiState.value = SignInUiState.Error("", true)
    }

    override fun handleEmail(email: String) {
        keeper.fieldState.value = keeper.fieldState.value.copy(email = email)
        keeper.validate()
    }

    override fun handlePassword(password: String) {
        keeper.fieldState.value = keeper.fieldState.value.copy(password = password)
        keeper.validate()
    }

}