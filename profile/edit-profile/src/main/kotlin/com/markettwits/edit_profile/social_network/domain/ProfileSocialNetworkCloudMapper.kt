package com.markettwits.edit_profile.social_network.domain

import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.cloud.model.profile.update.ChangeProfileInfoRequest

interface ProfileSocialNetworkCloudMapper {
    fun send(user: User, socialNetwork: UserSocialNetwork): ChangeProfileInfoRequest
    fun fetch(user: User): UserSocialNetwork
}