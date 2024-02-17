package com.markettwits.edit_profile.social_network.data

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.edit_profile.social_network.domain.ProfileSocialNetworkCloudMapper
import com.markettwits.edit_profile.social_network.domain.ProfileSocialNetworkRepository
import com.markettwits.edit_profile.social_network.domain.UserSocialNetwork
import com.markettwits.profile.data.AuthDataSource

class ProfileSocialNetworkRepositoryBase(
    private val mapper: ProfileSocialNetworkCloudMapper,
    private val auth: AuthDataSource,
    private val cloud: SportsouceApi
) : ProfileSocialNetworkRepository {
    override suspend fun send(userSocialNetwork: UserSocialNetwork): Result<String> {
        runCatching {
            val user = auth.auth()
            val token = auth.updateToken()
            val request = mapper.send(user, userSocialNetwork)
            cloud.changeProfileInfo(request, token)
        }.fold(onSuccess = {
            return Result.success("Данные успешно обновлены")
        }, onFailure = {
            return Result.failure(it)
        })
    }

    override suspend fun fetch(): Result<UserSocialNetwork> = runCatching {
        mapper.fetch(auth.auth())
    }
}