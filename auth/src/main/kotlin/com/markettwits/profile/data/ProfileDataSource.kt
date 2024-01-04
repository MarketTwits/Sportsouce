package com.markettwits.profile.data

import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.profile.presentation.sign_in.SignInUiState

interface AuthDataSource {
    suspend fun logIn(email : String, password : String) : SignInUiState
    suspend fun updatePassword(password: String)
    suspend fun updateToken() : String
    suspend fun auth() : User
    suspend fun show() : String
    suspend fun clear()
    suspend fun currentToken() : String
}