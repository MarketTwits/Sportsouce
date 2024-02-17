package com.markettwits.edit_profile.edit_profile_info.domain

import com.markettwits.edit_profile.edit_profile_info.domain.models.UserData
import com.markettwits.edit_profile.edit_profile_info.domain.models.UserDataContent

interface EditProfileInfoRepository {
    suspend fun send(userData: UserData): Result<String>
    suspend fun fetch(): Result<UserDataContent>
}