package com.markettwits.profile.internal.sign_in.domain

import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.profile.api.AuthDataSource

internal class SignInUseCaseBase(
    private val authDataSource: AuthDataSource
) : SignInUseCase {
    override suspend fun signIn(email: String, password: String): Result<User> =
        authDataSource.logIn(email.clearPhone(), password)
}