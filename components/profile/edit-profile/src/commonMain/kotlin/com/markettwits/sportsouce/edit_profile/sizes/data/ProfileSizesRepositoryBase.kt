package com.markettwits.sportsouce.edit_profile.sizes.data

import com.markettwits.sportsouce.auth.service.api.AuthDataSource
import com.markettwits.sportsouce.edit_profile.sizes.domain.ProfileSizes
import com.markettwits.sportsouce.edit_profile.sizes.domain.ProfileSizesRepository
import com.markettwits.sportsouce.profile.cloud.SportSauceNetworkPreferencesApi
import com.markettwits.sportsouce.profile.cloud.model.preferences.ProfileSizePreferences
import com.markettwits.sportsouce.profile.cloud.model.preferences.UpdateProfilePreferencesRequest
import kotlinx.coroutines.CancellationException

class ProfileSizesRepositoryBase(
    private val api: SportSauceNetworkPreferencesApi,
    private val auth: AuthDataSource,
) : ProfileSizesRepository {
    override suspend fun fetch(): Result<ProfileSizes> = request {
        (api.completeness(auth.updateToken().getOrThrow()).preferences ?: ProfileSizePreferences()).toDomain()
    }

    override suspend fun save(sizes: ProfileSizes): Result<ProfileSizes> = request {
        require(sizes.errors().isEmpty()) { "Проверьте размеры" }
        val data = sizes.normalized()
        val request = UpdateProfilePreferencesRequest(data.clothingSize, data.footLengthMm,
            data.shoeSizeUs, data.shoeSizeRu, data.shoeSizeJp)
        requireNotNull(api.update(request, auth.updateToken().getOrThrow()).preferences) {
            "Сервер не вернул сохранённые размеры"
        }.toDomain()
    }

    private fun ProfileSizePreferences.toDomain() = ProfileSizes(clothingSize, footLengthMm, shoeSizeRu, shoeSizeUs, shoeSizeJp)
    private suspend fun <T> request(block: suspend () -> T): Result<T> = try { Result.success(block()) }
    catch (cancelled: CancellationException) { throw cancelled }
    catch (error: Exception) { Result.failure(error) }
}
