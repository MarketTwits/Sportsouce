package com.markettwits.sportsouce.edit_profile.edit_social_network.domain.interactor


import com.markettwits.core_ui.items.extensions.flatMapCallback
import com.markettwits.sportsouce.edit_profile.edit_social_network.data.ProfileSocialNetworkRepository
import com.markettwits.sportsouce.edit_profile.edit_social_network.domain.UserSocialNetwork
import com.markettwits.sportsouce.edit_profile.edit_social_network.domain.handle.ProfileSocialNetworkHandler

class ProfileSocialNetworkInteractorBase(
    private val repository: ProfileSocialNetworkRepository,
    private val handler: ProfileSocialNetworkHandler
) :
    ProfileSocialNetworkInteractor {
    override suspend fun send(userSocialNetwork: UserSocialNetwork): Result<Unit> =
        handler.handle(userSocialNetwork).flatMapCallback {
            repository.send(it)
        }

    override suspend fun fetch(): Result<UserSocialNetwork> =
        repository.fetch()
}