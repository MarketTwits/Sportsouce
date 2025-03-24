package com.markettwits.unauthorized.domain

import com.markettwits.auth.cloud.model.sign_in.response.User

interface UnauthorizedProfileUseCase {

    suspend fun authorize(): Result<User>

    suspend fun exit()
}