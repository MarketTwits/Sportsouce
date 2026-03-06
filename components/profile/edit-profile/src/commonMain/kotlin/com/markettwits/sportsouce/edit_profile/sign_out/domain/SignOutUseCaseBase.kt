package com.markettwits.sportsouce.edit_profile.sign_out.domain

import com.markettwits.sportsouce.auth.service.api.AuthDataSource

class SignOutUseCaseBase(private val authRepository: AuthDataSource) : SignOutUseCase {
    override suspend fun signOut(): Result<Unit> = runCatching {
        authRepository.clear()
    }
}