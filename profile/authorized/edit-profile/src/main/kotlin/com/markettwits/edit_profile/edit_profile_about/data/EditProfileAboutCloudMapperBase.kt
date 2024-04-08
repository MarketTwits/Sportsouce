package com.markettwits.edit_profile.edit_profile_about.data

import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.cloud.model.profile.update.ChangeProfileInfoRequest

class EditProfileAboutCloudMapperBase : EditProfileAboutCloudMapper {
    override fun send(user: User, about: String): ChangeProfileInfoRequest =
        ChangeProfileInfoRequest(
            address = user.address ?: "",
            birthday = user.birthday,
            comment_for_address = about,
            email = user.email,
            favor = null, // You need to provide the correct value
            instagram = user.instagram,
            name = user.name,
            number = user.number,
            photo_id = user.photo_id,
            sex = user.sex,
            surname = user.surname,
            team = user.team,
            telegram = user.telegram,
            vk = user.vk,
            whatsapp = user.whatsapp,
            id = user.id
        )

    override fun fetch(user: User): String = user.comment_for_address ?: ""
}