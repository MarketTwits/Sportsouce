package com.markettwits.profile.data

import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.profile.presentation.ProfileUiState


interface ProfileDataSource {
    suspend fun profile() : ProfileUiState
    suspend fun authProfile() : String
    suspend fun checkAuth() : Boolean
    suspend fun exit()
}