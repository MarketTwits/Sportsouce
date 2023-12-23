package com.markettwits.profile.data

import com.markettwits.cloud.model.auth.common.AuthException
import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.profile.presentation.ProfileUiState

class BaseProfileDataSource(
    private val authDataSource: AuthDataSource
) : ProfileDataSource {
    override suspend fun profile(): ProfileUiState {
        return try {
            authDataSource.authInner()
            val user = authDataSource.auth()
            ProfileUiState.Base(user)
        } catch (e: Exception) {
            when (e) {
                is AuthException -> ProfileUiState.UnAuthorization
                else -> ProfileUiState.UnAuthorization
            }
        }
    }

    override suspend fun exit() {
        authDataSource.clear()
    }
}