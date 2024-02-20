package com.markettwits.edit_profile.social_network.domain

interface ProfileSocialNetworkRepository {
    suspend fun send(userSocialNetwork: UserSocialNetwork): Result<Unit>
    suspend fun fetch(): Result<UserSocialNetwork>
}