package com.markettwits.profile.domain

import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.profile.data.AuthDataSource
import kotlinx.coroutines.flow.Flow

class UserUseCaseBase(private val authDataSource: AuthDataSource) : UserUseCase {
    override suspend fun user(): Flow<Result<User>> = authDataSource.observeUser()
}