package com.markettwits.profile.presentation.component.authorized.data

import com.markettwits.profile.presentation.component.authorized.domain.UserProfile

interface AuthorizedProfileRepository {
    suspend fun profile(): Result<UserProfile>
}