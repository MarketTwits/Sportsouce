package com.markettwits.profile.internal.sign_in.domain

interface SignInUseCase {
    suspend fun signIn(email: String, password: String): Result<Unit>
}