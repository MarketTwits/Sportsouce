package com.markettwits.edit_profile.edit_profile_Image.data.mapper

import com.markettwits.auth.cloud.model.change.ChangeProfileInfoRequest
import com.markettwits.auth.cloud.model.sign_in.response.User
import com.markettwits.profile.cloud.model.update.UploadFileResponse

interface EditProfileImageCloudMapper {
    fun map(user: User, fileResponse: UploadFileResponse): ChangeProfileInfoRequest
}