package com.markettwits.profile.data

import com.markettwits.profile.presentation.ProfileUiState


interface ProfileDataSource {
    suspend fun profile() : ProfileUiState
    suspend fun exit()
}