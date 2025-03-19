package com.markettwits.edit_profile.edit_social_network.data.mapper

import com.markettwits.auth.cloud.model.change.ChangeProfileInfoRequest
import com.markettwits.auth.cloud.model.sign_in.response.User
import com.markettwits.edit_profile.edit_social_network.domain.UserSocialNetwork

interface ProfileSocialNetworkCloudMapper {
    fun send(user: User, socialNetwork: UserSocialNetwork): ChangeProfileInfoRequest
    fun fetch(user: User): UserSocialNetwork
}