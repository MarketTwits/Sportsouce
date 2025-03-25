package com.markettwits.sportsouce.auth.flow.internal.sign_in.domain

import com.markettwits.sportsouce.auth.cloud.model.sign_in.response.User

interface SignInUseCase {
    suspend fun signIn(email: String, password: String): Result<User>
}