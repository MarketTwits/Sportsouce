package com.markettwits.edit_profile.edit_social_network.data

import com.markettwits.core_ui.result.flatMapCallback
import com.markettwits.edit_profile.edit_social_network.data.mapper.ProfileSocialNetworkCloudMapper
import com.markettwits.edit_profile.edit_social_network.domain.UserSocialNetwork
import com.markettwits.profile.api.AuthDataSource

class ProfileSocialNetworkRepositoryBase(
    private val mapper: ProfileSocialNetworkCloudMapper,
    private val auth: AuthDataSource,
) : ProfileSocialNetworkRepository {
    override suspend fun send(userSocialNetwork: UserSocialNetwork): Result<Unit> =
        auth.user().flatMapCallback {
            val value = mapper.send(it, userSocialNetwork)
            auth.updateUser(value)
        }

    override suspend fun fetch(): Result<UserSocialNetwork> = auth.user().map { mapper.fetch(it) }
}