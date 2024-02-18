package com.markettwits.edit_profile.edit_profile_about.data

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.profile.data.AuthDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class EditProfileAboutRepositoryBase(
    private val cloud: SportsouceApi,
    private val authDataSource: AuthDataSource,
    private val mapper: EditProfileAboutCloudMapper
) : EditProfileAboutRepository {
    override suspend fun send(value: String): Result<Unit> =
        runCatching {
            val token = authDataSource.updateToken()
            val user = authDataSource.auth()
            cloud.changeProfileInfo(mapper.send(user, value), token).message
        }

    override suspend fun fetch(): Result<String> = runCatching {
        val profile = coroutineScope {
            withContext(Dispatchers.IO) {
                val deferredToken = async { authDataSource.updateToken() }
                val deferredProfile = async { cloud.auth(deferredToken.await()) }
                deferredProfile.await()
            }
        }
        mapper.fetch(profile)
    }
}