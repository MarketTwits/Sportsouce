package com.markettwits.profile.data

import com.markettwits.profile.presentation.deprecated.ProfileUiState

class BaseProfileDataSource(
    private val authDataSource: AuthDataSource
) : ProfileDataSource {
    override suspend fun profile(): ProfileUiState =
        authDataSource.user().fold(
            onSuccess = { ProfileUiState.Base(it) },
            onFailure = { ProfileUiState.Error(it.toString()) }
        )

    override suspend fun exit() {
        authDataSource.clear()
    }
}