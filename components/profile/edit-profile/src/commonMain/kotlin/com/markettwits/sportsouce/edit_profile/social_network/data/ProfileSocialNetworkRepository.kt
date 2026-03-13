package com.markettwits.sportsouce.edit_profile.social_network.data

import com.markettwits.sportsouce.edit_profile.social_network.domain.UserSocialNetwork

interface ProfileSocialNetworkRepository {
    suspend fun send(userSocialNetwork: UserSocialNetwork): Result<Unit>
    suspend fun fetch(): Result<UserSocialNetwork>
}