package com.markettwits.profile.domain

import com.markettwits.cloud.model.auth.sign_in.response.User
import kotlinx.coroutines.flow.Flow

interface UserUseCase {
    suspend fun user(): Flow<Result<User>>
}