package com.markettwits.edit_profile.edit_profile_Image.data.mapper

import com.markettwits.auth.cloud.model.change.ChangeProfileInfoRequest
import com.markettwits.auth.cloud.model.sign_in.response.User
import com.markettwits.profile.cloud.model.update.UploadFileResponse

internal class EditProfileImageCloudMapperBase : EditProfileImageCloudMapper {
    override fun map(user: User, fileResponse: UploadFileResponse): ChangeProfileInfoRequest =
        ChangeProfileInfoRequest(
            address = user.address ?: "",
            birthday = user.birthday,
            comment_for_address = user.comment_for_address,
            email = user.email ?: "",
            favor = null, // You need to provide the correct value
            instagram = user.instagram,
            name = user.name,
            number = user.number,
            photo_id = fileResponse.id,
            sex = user.sex,
            surname = user.surname,
            team = user.team,
            telegram = user.telegram,
            vk = user.vk,
            whatsapp = user.whatsapp,
            id = user.id
        )
}