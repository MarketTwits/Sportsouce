package com.markettwits.edit_profile.edit_profile_Image.data

import java.io.File

interface EditProfileImageRepository {
    suspend fun send(file: File): Result<Unit>
}