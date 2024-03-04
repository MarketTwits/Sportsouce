package com.markettwits.profile.data

import com.markettwits.profile.api.AuthDataSource
import com.markettwits.profile.presentation.deprecated.ProfileUiState

class BaseProfileDataSource(
    private val authDataSource: AuthDataSource
) : ProfileDataSource {
    override suspend fun profile(): Result<ProfileUiState> =
        authDataSource.user().mapCatching {
            ProfileUiState.Base(it)
        }

    override suspend fun exit() {
        authDataSource.clear()
    }
}