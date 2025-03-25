package com.markettwits.sportsouce.edit_profile.edit_profile_about.data

interface EditProfileAboutRepository {
    suspend fun send(value: String): Result<Unit>
    suspend fun fetch(): Result<String>
}