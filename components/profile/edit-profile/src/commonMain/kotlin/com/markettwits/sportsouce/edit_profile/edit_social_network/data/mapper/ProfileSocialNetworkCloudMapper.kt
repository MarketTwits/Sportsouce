package com.markettwits.sportsouce.edit_profile.edit_social_network.data.mapper

import com.markettwits.sportsouce.auth.cloud.model.change.ChangeProfileInfoRequest
import com.markettwits.sportsouce.auth.cloud.model.sign_in.response.User
import com.markettwits.sportsouce.edit_profile.edit_social_network.domain.UserSocialNetwork

interface ProfileSocialNetworkCloudMapper {
    fun send(user: User, socialNetwork: UserSocialNetwork): ChangeProfileInfoRequest
    fun fetch(user: User): UserSocialNetwork
}