package com.markettwits.profile.authorized.domain

import kotlinx.coroutines.flow.Flow

interface UserProfileInteractor {
    suspend fun userProfile(forced: Boolean): Flow<UserProfile>
}