package com.markettwits.sportsouce.profile.authorized.authorized.domain

import kotlinx.coroutines.flow.Flow

interface UserProfileInteractor {

    suspend fun userProfile(forced: Boolean): Flow<UserProfile>

}