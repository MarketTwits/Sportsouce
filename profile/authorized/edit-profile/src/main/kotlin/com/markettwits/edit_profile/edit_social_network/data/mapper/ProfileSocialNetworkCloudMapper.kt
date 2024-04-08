package com.markettwits.edit_profile.edit_social_network.data.mapper

import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.cloud.model.profile.update.ChangeProfileInfoRequest
import com.markettwits.edit_profile.edit_social_network.domain.UserSocialNetwork

interface ProfileSocialNetworkCloudMapper {
    fun send(user: User, socialNetwork: UserSocialNetwork): ChangeProfileInfoRequest
    fun fetch(user: User): UserSocialNetwork
}