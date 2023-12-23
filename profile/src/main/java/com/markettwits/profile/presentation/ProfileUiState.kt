package com.markettwits.profile.presentation

import com.markettwits.cloud.model.auth.sign_in.response.User

sealed class ProfileUiState{
    class Base(val user : User) : ProfileUiState()
    object UnAuthorization : ProfileUiState()
    object Initial : ProfileUiState()
}
