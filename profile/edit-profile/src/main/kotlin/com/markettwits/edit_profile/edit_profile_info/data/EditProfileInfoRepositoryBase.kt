package com.markettwits.edit_profile.edit_profile_info.data

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.edit_profile.edit_profile_info.domain.EditProfileInfoRepository
import com.markettwits.edit_profile.edit_profile_info.domain.models.UserData
import com.markettwits.edit_profile.edit_profile_info.domain.models.UserDataContent
import com.markettwits.profile.data.AuthDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class EditProfileInfoRepositoryBase(
    private val cloud: SportsouceApi,
    private val authDataSource: AuthDataSource,
    private val mapper: EditProfileInfoCloudMapper
) : EditProfileInfoRepository {
    override suspend fun send(userData: UserData): Result<String> =
        runCatching {
            val token = authDataSource.updateToken()
            val user = authDataSource.auth()
            cloud.changeProfileInfo(mapper.send(user, userData), token).message
        }

    override suspend fun fetch(): Result<UserDataContent> =
        runCatching {
            val (city, team, profile) = coroutineScope {
                withContext(Dispatchers.IO) {
                    val deferredCity = async { cloud.cities() }
                    val deferredTeam = async { cloud.teams() }
                    val deferredToken = async { authDataSource.updateToken() }
                    val deferredProfile = async { cloud.auth(deferredToken.await()) }
                    Triple(deferredCity.await(), deferredTeam.await(), deferredProfile.await())
                }
            }
            mapper.fetch(profile, team, city)
        }
}