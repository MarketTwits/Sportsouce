package com.markettwits.profile.internal.sign_in.presentation.component

import com.arkivanov.decompose.value.Value


internal interface SignInScreen {
    val state : Value<SignInUiState>
    val fieldState : Value<SignInFieldUiState>
    fun back()
    fun signUp()
    fun forgotPassword()
    fun logIn()
    fun messageHasBeenShowed()
    fun handleEmail(email : String)
    fun handlePhone(phone: String)
    fun handlePassword(password: String)
}