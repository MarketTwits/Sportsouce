package com.markettwits.unauthorized.domain

import com.markettwits.auth.cloud.model.sign_in.response.User
import com.markettwits.profile.api.AuthDataSource

internal class UnauthorizedProfileUseCaseBase(
    private val authDataSource: AuthDataSource
) : UnauthorizedProfileUseCase {

    override suspend fun authorize(): Result<User> = authDataSource.user()

    override suspend fun exit() {
        authDataSource.clear()
    }
}