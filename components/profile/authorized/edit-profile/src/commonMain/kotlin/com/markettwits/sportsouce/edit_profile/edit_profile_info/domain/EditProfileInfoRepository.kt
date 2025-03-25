package com.markettwits.sportsouce.edit_profile.edit_profile_info.domain

import com.markettwits.sportsouce.edit_profile.edit_profile_info.domain.models.UserData
import com.markettwits.sportsouce.edit_profile.edit_profile_info.domain.models.UserDataContent
import kotlinx.coroutines.flow.Flow

interface EditProfileInfoRepository {
    suspend fun send(userData: UserData): Result<Unit>
    suspend fun fetch(): Flow<UserDataContent>
}