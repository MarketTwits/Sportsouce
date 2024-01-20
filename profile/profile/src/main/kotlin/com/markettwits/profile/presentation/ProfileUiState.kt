package com.markettwits.profile.presentation

import com.markettwits.cloud.model.auth.sign_in.response.User
import kotlinx.serialization.Serializable

@Serializable
sealed interface ProfileUiState{
    @Serializable
    data class Base(val user : User) : ProfileUiState
    @Serializable
    data class Error(val message : String) : ProfileUiState
    @Serializable
    data object Loading : ProfileUiState
}
