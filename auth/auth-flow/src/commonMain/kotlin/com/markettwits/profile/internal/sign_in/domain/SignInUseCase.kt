package com.markettwits.profile.internal.sign_in.domain

import com.markettwits.auth.cloud.model.sign_in.response.User

interface SignInUseCase {
    suspend fun signIn(email: String, password: String): Result<User>
}