package com.markettwits.sportsouce.edit_profile.edit_profile_Image.data.mapper

import com.markettwits.sportsouce.auth.cloud.model.change.ChangeProfileInfoRequest
import com.markettwits.sportsouce.auth.cloud.model.sign_in.response.User
import com.markettwits.sportsouce.profile.cloud.model.update.UploadFileResponse

interface EditProfileImageCloudMapper {
    fun map(user: User, fileResponse: UploadFileResponse): ChangeProfileInfoRequest
}