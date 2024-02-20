package com.markettwits.edit_profile.social_network.data

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.core_ui.result.flatMapCallback
import com.markettwits.edit_profile.social_network.domain.ProfileSocialNetworkCloudMapper
import com.markettwits.edit_profile.social_network.domain.ProfileSocialNetworkRepository
import com.markettwits.edit_profile.social_network.domain.UserSocialNetwork
import com.markettwits.profile.data.AuthDataSource

class ProfileSocialNetworkRepositoryBase(
    private val mapper: ProfileSocialNetworkCloudMapper,
    private val auth: AuthDataSource,
    private val cloud: SportsouceApi
) : ProfileSocialNetworkRepository {
    override suspend fun send(userSocialNetwork: UserSocialNetwork): Result<Unit> =
        auth.user().flatMapCallback {
            val value = mapper.send(it, userSocialNetwork)
            auth.updateUser(value)
        }

    override suspend fun fetch(): Result<UserSocialNetwork> = auth.user().map { mapper.fetch(it) }
}