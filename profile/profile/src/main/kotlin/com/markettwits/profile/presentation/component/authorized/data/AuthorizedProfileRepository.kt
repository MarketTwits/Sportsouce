package com.markettwits.profile.presentation.component.authorized.data

import com.markettwits.profile.presentation.component.authorized.domain.UserProfile
import kotlinx.coroutines.flow.Flow

interface AuthorizedProfileRepository {
    suspend fun profile(forced: Boolean = false): Flow<UserProfile>
}