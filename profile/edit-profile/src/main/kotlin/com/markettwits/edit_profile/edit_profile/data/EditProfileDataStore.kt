package com.markettwits.edit_profile.edit_profile.data

import com.markettwits.edit_profile.edit_profile.presentation.EditProfileUiPage
import com.markettwits.profile.presentation.component.edit_profile.presentation.EditProfileUiState

interface EditProfileDataStore {
    suspend fun changeProfileInfo(current : List<EditProfileUiPage>) : EditProfileUiState
    suspend fun profile() : EditProfileUiState
}