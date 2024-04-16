package com.markettwits.edit_profile.edit_profile_info.data


import com.markettwits.core_ui.items.result.flatMapCallback
import com.markettwits.edit_profile.edit_profile_info.domain.EditProfileInfoRepository
import com.markettwits.edit_profile.edit_profile_info.domain.models.UserData
import com.markettwits.edit_profile.edit_profile_info.domain.models.UserDataContent
import com.markettwits.profile.api.AuthDataSource
import com.markettwits.teams_city.data.TeamsCityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class EditProfileInfoRepositoryBase(
    private val authDataSource: AuthDataSource,
    private val teamCity: TeamsCityRepository,
    private val mapper: EditProfileInfoCloudMapper
) : EditProfileInfoRepository {
    override suspend fun send(userData: UserData): Result<Unit> =
        authDataSource.user().flatMapCallback {
            authDataSource.updateUser(mapper.send(it, userData))
        }

    override suspend fun fetch(): Flow<UserDataContent> =
        teamCity.cityFlow().combine(teamCity.teamsFlow()) { cities, teams ->
            val user = authDataSource.user().getOrThrow()
            mapper.fetch(user, teams, cities)
        }
}