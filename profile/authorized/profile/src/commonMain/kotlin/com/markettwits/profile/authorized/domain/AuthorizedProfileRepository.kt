package com.markettwits.profile.authorized.domain

import kotlinx.coroutines.flow.Flow

interface AuthorizedProfileRepository {
    suspend fun profile(forced: Boolean = false): Flow<UserProfile>
}