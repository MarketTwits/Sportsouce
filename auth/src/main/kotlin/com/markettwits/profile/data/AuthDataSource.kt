package com.markettwits.profile.data

import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.profile.presentation.sign_in.SignInUiState
import com.markettwits.profile.presentation.sign_up.domain.SignUpStatement

interface AuthDataSource {
    suspend fun register(signUpStatement: SignUpStatement) : Result<String>
    suspend fun logIn(email : String, password : String) : SignInUiState
    suspend fun updatePassword(password: String)
    suspend fun updateToken() : String
    suspend fun auth() : User
    suspend fun clear()

    @Deprecated("use update token with validation")
    suspend fun currentToken() : String
}