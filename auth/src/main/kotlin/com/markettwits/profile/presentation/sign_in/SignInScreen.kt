package com.markettwits.profile.presentation.sign_in

import com.arkivanov.decompose.value.Value


interface SignInScreen {
    val state : Value<SignInUiState>
    val fieldState : Value<SignInFieldUiState>
    fun logIn()
    fun messageHasBeenShowed()
    fun handleEmail(email : String)
    fun handlePassword(password: String)
}