package com.markettwits.profile.domain

import com.markettwits.cloud.model.auth.sign_in.response.User

interface UserUseCase {
    suspend fun user(): Result<User>
}