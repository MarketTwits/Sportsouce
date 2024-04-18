package com.markettwits.profile.internal.sign_in.domain

import com.markettwits.cloud.model.auth.sign_in.response.User

interface SignInUseCase {
    suspend fun signIn(email: String, password: String): Result<User>
}