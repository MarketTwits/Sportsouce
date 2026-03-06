package com.markettwits.sportsouce.edit_profile.image.data

interface EditProfileImageRepository {
    suspend fun send(data: ByteArray, lastModified: Long): Result<Unit>
}