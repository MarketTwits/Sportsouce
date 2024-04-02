package com.markettwits.profile.data

import com.markettwits.profile.presentation.component.base.ProfileUiState


interface ProfileDataSource {
    suspend fun profile(): Result<ProfileUiState>
    suspend fun exit()
}