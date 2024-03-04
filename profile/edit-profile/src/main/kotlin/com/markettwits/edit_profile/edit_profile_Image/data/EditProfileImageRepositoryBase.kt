package com.markettwits.edit_profile.edit_profile_Image.data

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.cloud.model.image.UploadFileResponse
import com.markettwits.core_ui.result.flatMapCallback
import com.markettwits.edit_profile.edit_profile_Image.data.mapper.EditProfileImageCloudMapper
import com.markettwits.profile.api.AuthDataSource
import java.io.File

internal class EditProfileImageRepositoryBase(
    private val cloud: SportsouceApi,
    private val authDataSource: AuthDataSource,
    private val mapper: EditProfileImageCloudMapper
) : EditProfileImageRepository {
    override suspend fun send(file: File): Result<Unit> =
        uploadFile(file).flatMapCallback { fileResponse ->
            authDataSource.user().flatMapCallback { user ->
                authDataSource.updateUser(mapper.map(user, fileResponse))
            }
        }

    private suspend fun uploadFile(file: File): Result<UploadFileResponse> =
        runCatching { cloud.uploadFile(file) }

    private fun fake(file: File) = Result.failure<UploadFileResponse>(Exception(""))
}