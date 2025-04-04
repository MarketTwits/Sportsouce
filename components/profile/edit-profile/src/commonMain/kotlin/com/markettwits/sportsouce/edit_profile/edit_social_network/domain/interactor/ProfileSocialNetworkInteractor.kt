package com.markettwits.sportsouce.edit_profile.edit_social_network.domain.interactor

import com.markettwits.sportsouce.edit_profile.edit_social_network.domain.UserSocialNetwork

interface ProfileSocialNetworkInteractor {
    suspend fun send(userSocialNetwork: UserSocialNetwork): Result<Unit>
    suspend fun fetch(): Result<UserSocialNetwork>
}