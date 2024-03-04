package com.markettwits.edit_profile.edit_profile_info.data

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.core_ui.result.flatMapCallback
import com.markettwits.edit_profile.edit_profile_info.domain.EditProfileInfoRepository
import com.markettwits.edit_profile.edit_profile_info.domain.models.UserData
import com.markettwits.edit_profile.edit_profile_info.domain.models.UserDataContent
import com.markettwits.profile.api.AuthDataSource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class EditProfileInfoRepositoryBase(
    private val cloud: SportsouceApi,
    private val authDataSource: AuthDataSource,
    private val mapper: EditProfileInfoCloudMapper
) : EditProfileInfoRepository {
    override suspend fun send(userData: UserData): Result<Unit> =
        authDataSource.user().flatMapCallback {
            authDataSource.updateUser(mapper.send(it, userData))
        }

    override suspend fun fetch(): Result<UserDataContent> =
        authDataSource.user().flatMapCallback { user ->
            runCatching {
                val (city, team) = coroutineScope {
                    val deferredCity = async { cloud.cities() }
                    val deferredTeam = async { cloud.teams() }
                    Pair(deferredCity.await(), deferredTeam.await())
                }
                mapper.fetch(user, team, city)
            }
        }
}