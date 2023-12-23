package com.markettwits.profile.presentation.sign_in

sealed class SignInUiState{
    data object Success : SignInUiState()
    data class Error(
        val message : String,
        val messageShow : Boolean = false
    ) : SignInUiState()
    data object Loading : SignInUiState()
    data object Initial : SignInUiState()
}
data class SignInFieldUiState(
    val email : String,
    val password : String,
    val enabled : Boolean
)