package com.markettwits.profile.presentation.sign_in

import com.arkivanov.decompose.value.Value


interface SignInScreen {
    val state : Value<String>
    fun logIn(password : String, email : String)
    fun show()
}