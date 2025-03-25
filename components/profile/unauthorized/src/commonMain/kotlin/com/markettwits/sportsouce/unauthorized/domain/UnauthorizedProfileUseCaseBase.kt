package com.markettwits.sportsouce.unauthorized.domain

import com.markettwits.sportsouce.auth.cloud.model.sign_in.response.User
import com.markettwits.sportsouce.auth.service.api.AuthDataSource

internal class UnauthorizedProfileUseCaseBase(
    private val authDataSource: AuthDataSource
) : UnauthorizedProfileUseCase {

    override suspend fun authorize(): Result<User> = authDataSource.user()

    override suspend fun exit() {
        authDataSource.clear()
    }
}