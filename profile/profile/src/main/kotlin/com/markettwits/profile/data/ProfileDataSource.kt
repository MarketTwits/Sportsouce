package com.markettwits.profile.data

import com.markettwits.profile.presentation.deprecated.ProfileUiState


interface ProfileDataSource {
    suspend fun profile(): Result<ProfileUiState>
    suspend fun exit()
}