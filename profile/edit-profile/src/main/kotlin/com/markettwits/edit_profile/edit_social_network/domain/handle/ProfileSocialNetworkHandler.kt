package com.markettwits.edit_profile.edit_social_network.domain.handle

import com.markettwits.edit_profile.edit_social_network.domain.UserSocialNetwork

interface ProfileSocialNetworkHandler {
    fun handle(userSocialNetwork: UserSocialNetwork): Result<UserSocialNetwork>
}