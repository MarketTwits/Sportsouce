package com.markettwits.profile.presentation

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class MockProfileScreenComponent : ProfileScreen{
    override val nameState: Value<ProfileUiState> = MutableValue(ProfileUiState.Loading)
    override fun goToSignInScreen() {

    }

    override fun exit() {

    }

    override fun init() {

    }
}