package com.markettwits.profile.internal.sign_in.domain

import com.markettwits.profile.api.AuthDataSource

internal class SignInUseCaseBase(
    private val authDataSource: AuthDataSource
) : SignInUseCase {
    override suspend fun signIn(email: String, password: String): Result<Unit> =
        authDataSource.logIn(email, password)
}