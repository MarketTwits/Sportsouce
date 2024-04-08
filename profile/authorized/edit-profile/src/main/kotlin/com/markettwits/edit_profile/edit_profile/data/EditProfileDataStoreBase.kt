package com.markettwits.edit_profile.edit_profile.data

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.cloud.exception.networkExceptionHandler
import com.markettwits.edit_profile.edit_profile.presentation.EditProfileUiPage
import com.markettwits.edit_profile.edit_profile.presentation.mapper.RemoteUserToEditProfileMapper
import com.markettwits.profile.api.AuthDataSource
import com.markettwits.profile.presentation.component.edit_profile.presentation.EditProfileUiState
import com.markettwits.teams_city.data.TeamsCityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class EditProfileDataStoreBase(
    private val mapper: RemoteUserToEditProfileMapper,
    private val authDataSource: AuthDataSource,
    private val teamsCityRepository: TeamsCityRepository,
    private val cloud: SportsouceApi
) : EditProfileDataStore {
    override suspend fun changeProfileInfo(
        current: List<EditProfileUiPage>,
    ): EditProfileUiState {
        return try {
            val token = authDataSource.updateToken()
            cloud.changeProfileInfo(mapper.map(current), token.getOrThrow())
            profile()
        } catch (e: Exception) {
            EditProfileUiState.Error(message = networkExceptionHandler(e).message.toString())
        }
    }

    override suspend fun profile(): EditProfileUiState {
        return try {
            val (city, team, profile) = coroutineScope {
                withContext(Dispatchers.IO) {
                    val deferredCity = async { teamsCityRepository.city().getOrThrow() }
                    val deferredTeam = async { teamsCityRepository.teams().getOrThrow() }
                    val deferredToken = async { authDataSource.updateToken() }
                    val deferredProfile = async { cloud.auth(deferredToken.await().getOrThrow()) }
                    Triple(deferredCity.await(), deferredTeam.await(), deferredProfile.await())
                }
            }
            mapper.map(city, team, profile)
        } catch (e: Exception) {
            mapper.map(e)
        }

    }


}