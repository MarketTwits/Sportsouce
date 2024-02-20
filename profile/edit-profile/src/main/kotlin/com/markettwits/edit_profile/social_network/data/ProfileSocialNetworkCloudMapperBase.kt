package com.markettwits.edit_profile.social_network.data

import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.cloud.model.profile.ChangeProfileInfoRequest
import com.markettwits.edit_profile.social_network.domain.ProfileSocialNetworkCloudMapper
import com.markettwits.edit_profile.social_network.domain.UserSocialNetwork

class ProfileSocialNetworkCloudMapperBase : ProfileSocialNetworkCloudMapper {
    override fun send(user: User, socialNetwork: UserSocialNetwork): ChangeProfileInfoRequest =
        ChangeProfileInfoRequest(
            address = user.address ?: "",
            birthday = user.birthday,
            comment_for_address = user.comment_for_address,
            email = user.email,
            favor = null, // You need to provide the correct value
            instagram = socialNetwork.instagram,
            name = user.name,
            number = user.number,
            photo_id = user.photo_id,
            sex = user.sex,
            surname = user.surname,
            team = user.team,
            telegram = socialNetwork.telegram,
            vk = socialNetwork.vk,
            whatsapp = socialNetwork.whatsApp,
            id = user.id
        )

    override fun fetch(user: User): UserSocialNetwork =
        UserSocialNetwork(
            telegram = user.telegram ?: "",
            whatsApp = user.whatsapp ?: "",
            vk = user.vk ?: "",
            instagram = user.instagram ?: ""
        )
}