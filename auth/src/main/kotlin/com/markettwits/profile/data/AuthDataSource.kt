package com.markettwits.profile.data

import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.cloud.model.profile.ChangeProfileInfoRequest
import com.markettwits.profile.presentation.sign_in.SignInUiState
import com.markettwits.profile.presentation.sign_up.domain.SignUpStatement
import kotlinx.coroutines.flow.Flow

interface AuthDataSource {
    suspend fun register(signUpStatement: SignUpStatement) : Result<String>
    suspend fun logIn(email : String, password : String) : SignInUiState
    suspend fun updatePassword(password: String)
    suspend fun updateToken() : String
    suspend fun observeUser(): Flow<Result<User>>
    suspend fun user(): Result<User>
    suspend fun updateUser(request: ChangeProfileInfoRequest): Result<Unit>
    suspend fun auth() : User
    suspend fun clear()
}