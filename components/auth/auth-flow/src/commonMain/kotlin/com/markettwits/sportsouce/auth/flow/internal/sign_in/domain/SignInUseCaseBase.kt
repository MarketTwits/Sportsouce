package com.markettwits.sportsouce.auth.flow.internal.sign_in.domain

import com.markettwits.sportsouce.auth.cloud.model.sign_in.response.User
import com.markettwits.sportsouce.auth.service.api.AuthDataSource

internal class SignInUseCaseBase(
    private val authDataSource: AuthDataSource
) : SignInUseCase {
    override suspend fun signIn(email: String, password: String): Result<User> =
        authDataSource.logIn(email.clearPhone(), password)
}