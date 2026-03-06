package com.markettwits.sportsouce.edit_profile.social_network.domain.handle

import com.markettwits.sportsouce.edit_profile.social_network.domain.UserSocialNetwork

interface ProfileSocialNetworkHandler {
    fun handle(userSocialNetwork: UserSocialNetwork): Result<UserSocialNetwork>
}