package com.markettwits.sportsouce.edit_profile.sign_out.domain

import com.markettwits.sportsouce.auth.service.api.AuthDataSource
import com.markettwits.sportsouce.starts.recent.domain.StartRecentRepository

class SignOutUseCaseBase(
    private val authRepository: AuthDataSource,
    private val recentStartsRepository: StartRecentRepository,
) : SignOutUseCase {
    override suspend fun signOut(): Result<Unit> = runCatching {
        recentStartsRepository.clear()
        authRepository.clear()
    }
}