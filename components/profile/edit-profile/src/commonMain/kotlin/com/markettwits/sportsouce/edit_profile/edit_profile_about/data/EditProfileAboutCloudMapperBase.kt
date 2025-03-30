package com.markettwits.sportsouce.edit_profile.edit_profile_about.data

import com.markettwits.sportsouce.auth.cloud.model.change.ChangeProfileInfoRequest
import com.markettwits.sportsouce.auth.cloud.model.sign_in.response.User

class EditProfileAboutCloudMapperBase : EditProfileAboutCloudMapper {
    override fun send(user: User, about: String): ChangeProfileInfoRequest =
        ChangeProfileInfoRequest(
            address = user.address ?: "",
            birthday = user.birthday,
            commentForAddress = about,
            email = user.email ?: "",
            favor = null, // You need to provide the correct value
            instagram = user.instagram,
            name = user.name,
            number = user.number,
            photoId = user.photoId,
            sex = user.sex,
            surname = user.surname,
            team = user.team,
            telegram = user.telegram,
            vk = user.vk,
            whatsapp = user.whatsapp,
            id = user.id
        )

    override fun fetch(user: User): String = user.commentForAddress ?: ""
}