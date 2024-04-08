package com.markettwits.unauthorized.presentation.components

import com.markettwits.cloud.model.auth.sign_in.response.User
import kotlinx.serialization.Serializable

@Serializable
sealed interface UnAuthorizedProfileUiState {
    @Serializable
    data class Base(val user: User) : UnAuthorizedProfileUiState

    @Serializable
    data class Error(val message: String) : UnAuthorizedProfileUiState

    @Serializable
    data object Loading : UnAuthorizedProfileUiState
}