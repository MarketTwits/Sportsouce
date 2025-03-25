package com.markettwits.sportsouce.edit_profile.edit_profile_Image.data

interface EditProfileImageRepository {
    suspend fun send(data: ByteArray, lastModified: Long): Result<Unit>
}