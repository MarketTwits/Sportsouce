package com.markettwits.edit_profile.edit_social_network.data

import com.markettwits.edit_profile.edit_social_network.domain.UserSocialNetwork

interface ProfileSocialNetworkRepository {
    suspend fun send(userSocialNetwork: UserSocialNetwork): Result<Unit>
    suspend fun fetch(): Result<UserSocialNetwork>
}