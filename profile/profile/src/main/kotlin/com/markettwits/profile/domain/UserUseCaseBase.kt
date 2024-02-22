package com.markettwits.profile.domain

import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.profile.data.AuthDataSource

class UserUseCaseBase(
    private val authDataSource: AuthDataSource
) : UserUseCase {
    override suspend fun user(): Result<User> = authDataSource.user()
}