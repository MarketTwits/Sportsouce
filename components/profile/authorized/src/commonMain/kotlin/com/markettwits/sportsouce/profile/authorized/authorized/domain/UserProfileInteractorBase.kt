package com.markettwits.sportsouce.profile.authorized.authorized.domain

import com.markettwits.sportsouce.profile.registrations.list.domain.StartOrderInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserProfileInteractorBase(private val repository: AuthorizedProfileRepository) :
    UserProfileInteractor {
    override suspend fun userProfile(forced: Boolean): Flow<UserProfile> {
        return repository.profile(forced).map { userProfile ->
            val sortedUserRegistry = sortUserRegistry(userProfile.activity.userRegistry)
            userProfile.copy(activity = userProfile.activity.copy(userRegistry = sortedUserRegistry))
        }
    }

    private fun sortUserRegistry(userRegistry: List<StartOrderInfo>): List<StartOrderInfo> {
        return userRegistry
    }
}