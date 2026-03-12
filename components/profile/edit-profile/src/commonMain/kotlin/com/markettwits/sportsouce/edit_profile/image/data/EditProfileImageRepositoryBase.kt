package com.markettwits.sportsouce.edit_profile.image.data

import com.markettwits.core_ui.items.extensions.flatMapCallback
import com.markettwits.sportsouce.auth.service.api.AuthDataSource
import com.markettwits.sportsouce.edit_profile.image.data.mapper.EditProfileImageCloudMapper
import com.markettwits.sportsouce.profile.cloud.SportSauceNetworkProfileApi
import com.markettwits.sportsouce.profile.cloud.model.update.UploadFileResponse

internal class EditProfileImageRepositoryBase(
    private val cloud: SportSauceNetworkProfileApi,
    private val authDataSource: AuthDataSource,
    private val mapper: EditProfileImageCloudMapper
) : EditProfileImageRepository {
    override suspend fun send(
        data: ByteArray,
        lastModified: Long,
        fileName: String,
        contentType: String,
    ): Result<Unit> =
        uploadFile(
            data = data,
            lastModified = lastModified,
            fileName = fileName,
            contentType = contentType
        ).flatMapCallback { fileResponse ->
            authDataSource.user().flatMapCallback { user ->
                authDataSource.updateUser(mapper.map(user, fileResponse))
            }
        }

    private suspend fun uploadFile(
        data: ByteArray,
        lastModified: Long,
        fileName: String,
        contentType: String,
    ): Result<UploadFileResponse> =
        runCatching { cloud.uploadFile(data, lastModified, fileName, contentType) }
}
