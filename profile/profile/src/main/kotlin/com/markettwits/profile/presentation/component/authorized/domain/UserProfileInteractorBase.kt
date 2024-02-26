package com.markettwits.profile.presentation.component.authorized.domain

import com.markettwits.profile.presentation.component.authorized.data.AuthorizedProfileRepository
import com.markettwits.registrations.registrations.domain.StartOrderInfo
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
        val sortedItems = userRegistry.filterNot { it.payment.payment }
        val unsortedItems = userRegistry.filter { it.payment.payment }
        return sortedItems + unsortedItems
    }
}