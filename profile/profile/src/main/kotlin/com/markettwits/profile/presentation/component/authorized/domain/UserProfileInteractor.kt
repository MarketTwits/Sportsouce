package com.markettwits.profile.presentation.component.authorized.domain

import kotlinx.coroutines.flow.Flow

interface UserProfileInteractor {
    suspend fun userProfile(forced: Boolean): Flow<UserProfile>
}