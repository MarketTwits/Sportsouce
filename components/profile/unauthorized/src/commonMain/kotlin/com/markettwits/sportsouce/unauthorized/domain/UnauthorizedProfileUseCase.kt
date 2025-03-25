package com.markettwits.sportsouce.unauthorized.domain

import com.markettwits.sportsouce.auth.cloud.model.sign_in.response.User

interface UnauthorizedProfileUseCase {

    suspend fun authorize(): Result<User>

    suspend fun exit()
}