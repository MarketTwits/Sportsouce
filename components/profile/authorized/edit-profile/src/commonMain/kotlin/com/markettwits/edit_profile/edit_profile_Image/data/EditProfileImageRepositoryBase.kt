package com.markettwits.edit_profile.edit_profile_Image.data

import com.markettwits.core_ui.items.extensions.flatMapCallback
import com.markettwits.edit_profile.edit_profile_Image.data.mapper.EditProfileImageCloudMapper
import com.markettwits.profile.api.AuthDataSource
import com.markettwits.profile.cloud.SportSauceNetworkProfileApi
import com.markettwits.profile.cloud.model.update.UploadFileResponse

internal class EditProfileImageRepositoryBase(
    private val cloud: SportSauceNetworkProfileApi,
    private val authDataSource: AuthDataSource,
    private val mapper: EditProfileImageCloudMapper
) : EditProfileImageRepository {
    override suspend fun send(data: ByteArray, lastModified: Long): Result<Unit> =
        uploadFile(data, lastModified).flatMapCallback { fileResponse ->
            authDataSource.user().flatMapCallback { user ->
                authDataSource.updateUser(mapper.map(user, fileResponse))
            }
        }

    private suspend fun uploadFile(
        data: ByteArray,
        lastModified: Long
    ): Result<UploadFileResponse> =
        runCatching { cloud.uploadFile(data, lastModified) }
}