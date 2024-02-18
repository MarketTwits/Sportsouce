package com.markettwits.profile.data

import com.markettwits.profile.presentation.ProfileUiState

class BaseProfileDataSource(
    private val authDataSource: AuthDataSource
) : ProfileDataSource {
    override suspend fun profile(): ProfileUiState {
        return try {
            val user = authDataSource.auth()
            ProfileUiState.Base(user)
        } catch (e: Exception) {
            when (e) {
                else -> ProfileUiState.Error(e.toString())
            }
        }
    }

    override suspend fun exit() {
        authDataSource.clear()
    }
}