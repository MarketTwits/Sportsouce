package com.markettwits.sportsouce.edit_profile.sizes.domain

interface ProfileSizesRepository {
    suspend fun fetch(): Result<ProfileSizes>
    suspend fun save(sizes: ProfileSizes): Result<ProfileSizes>
}
