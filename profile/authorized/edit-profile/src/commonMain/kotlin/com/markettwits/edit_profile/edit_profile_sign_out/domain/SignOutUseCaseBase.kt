package com.markettwits.edit_profile.edit_profile_sign_out.domain

import com.markettwits.profile.api.AuthDataSource

class SignOutUseCaseBase(private val authRepository: AuthDataSource) : SignOutUseCase {
    override suspend fun signOut(): Result<Unit> = runCatching {
        authRepository.clear()
    }
}