package com.markettwits.edit_profile.edit_profile_Image.data

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.cloud.model.image.UploadFileResponse
import com.markettwits.edit_profile.edit_profile_Image.data.mapper.EditProfileImageCloudMapper
import com.markettwits.profile.data.AuthDataSource
import java.io.File

internal class EditProfileImageRepositoryBase(
    private val cloud: SportsouceApi,
    private val authDataSource: AuthDataSource,
    private val mapper: EditProfileImageCloudMapper
) : EditProfileImageRepository {
    override suspend fun send(file: File): Result<Unit> =
        runCatching {
            val user = authDataSource.auth()
            val token = authDataSource.updateToken()
            uploadFile(file).fold(
                onSuccess = { fileResponse ->
                    cloud.changeProfileInfo(
                        profile = mapper.map(user, fileResponse),
                        token = token
                    )
                    return Result.success(Unit)
                }, onFailure = {
                    return Result.failure(it)
                })
        }
    private suspend fun uploadFile(file: File): Result<UploadFileResponse> =
        runCatching { cloud.uploadFile(file) }

    private fun fake(file: File) = Result.failure<UploadFileResponse>(Exception(""))
}