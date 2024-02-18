package com.markettwits.edit_profile.edit_profile_Image.data.mapper

import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.cloud.model.image.UploadFileResponse
import com.markettwits.cloud.model.profile.ChangeProfileInfoRequest

interface EditProfileImageCloudMapper {
    fun map(user: User, fileResponse: UploadFileResponse): ChangeProfileInfoRequest
}