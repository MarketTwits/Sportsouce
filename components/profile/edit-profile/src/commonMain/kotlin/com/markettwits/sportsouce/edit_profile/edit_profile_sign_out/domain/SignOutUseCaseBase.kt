package com.markettwits.sportsouce.edit_profile.edit_profile_sign_out.domain

import com.markettwits.sportsouce.auth.service.api.AuthDataSource

class SignOutUseCaseBase(private val authRepository: AuthDataSource) : SignOutUseCase {
    override suspend fun signOut(): Result<Unit> = runCatching {
        authRepository.clear()
    }
}