package com.markettwits.sportsouce.edit_profile.edit_profile_Image.data.mapper

import com.markettwits.sportsouce.auth.cloud.model.change.ChangeProfileInfoRequest
import com.markettwits.sportsouce.auth.cloud.model.sign_in.response.User
import com.markettwits.sportsouce.profile.cloud.model.update.UploadFileResponse

internal class EditProfileImageCloudMapperBase : EditProfileImageCloudMapper {
    override fun map(user: User, fileResponse: UploadFileResponse): ChangeProfileInfoRequest =
        ChangeProfileInfoRequest(
            address = user.address ?: "",
            birthday = user.birthday,
            commentForAddress = user.commentForAddress,
            email = user.email ?: "",
            favor = null, // You need to provide the correct value
            instagram = user.instagram,
            name = user.name,
            number = user.number,
            photoId = fileResponse.id,
            sex = user.sex,
            surname = user.surname,
            team = user.team,
            telegram = user.telegram,
            vk = user.vk,
            whatsapp = user.whatsapp,
            id = user.id
        )
}