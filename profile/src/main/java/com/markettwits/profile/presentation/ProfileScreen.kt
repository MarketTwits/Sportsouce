package com.markettwits.profile.presentation

import com.arkivanov.decompose.value.Value

interface ProfileScreen {
    val nameState : Value<ProfileUiState>
    fun goToSignInScreen()
    fun exit()
    fun init()
}