package com.markettwits.sportsouce.edit_profile.edit_profile_info.data


import com.markettwits.core_ui.items.extensions.flatMapCallback
import com.markettwits.sportsouce.auth.service.api.AuthDataSource
import com.markettwits.sportsouce.edit_profile.edit_profile_info.domain.EditProfileInfoRepository
import com.markettwits.sportsouce.edit_profile.edit_profile_info.domain.models.UserData
import com.markettwits.sportsouce.edit_profile.edit_profile_info.domain.models.UserDataContent
import com.markettwits.sportsouce.teams_city.domain.TeamsCityRepository
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