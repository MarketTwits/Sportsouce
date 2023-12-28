package com.markettwits.profile.presentation.component.edit_profile.data

import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.profile.presentation.component.edit_profile.presentation.EditProfileUiPage

interface EditProfileDataSource {
    suspend fun changeProfileInfo(current : List<EditProfileUiPage>, base : User)
}