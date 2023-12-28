package com.markettwits.profile.data

import com.markettwits.cloud.model.auth.common.AuthException
import com.markettwits.profile.presentation.ProfileUiState

class BaseProfileDataSource(
    private val authDataSource: AuthDataSource
) : ProfileDataSource {
    override suspend fun profile(): ProfileUiState {
        return try {
            authDataSource.updateToken()
            val user = authDataSource.auth()
            ProfileUiState.Base(user)
        } catch (e: Exception) {
            when (e) {
                else -> ProfileUiState.Error(e.toString())
            }
        }
    }

    override suspend fun authProfile(): String {
        return try {
            authDataSource.updateToken()
            val user = authDataSource.auth()
            user.name
        } catch (e: Exception) {
            return e.toString()
        }
    }

    override suspend fun checkAuth(): Boolean {
        return try {
            authDataSource.currentToken()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun exit() {
        authDataSource.clear()
    }
}