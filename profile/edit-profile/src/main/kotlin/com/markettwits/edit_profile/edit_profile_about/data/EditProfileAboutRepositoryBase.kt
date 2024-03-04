package com.markettwits.edit_profile.edit_profile_about.data

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.core_ui.result.flatMapCallback
import com.markettwits.profile.api.AuthDataSource

class EditProfileAboutRepositoryBase(
    private val cloud: SportsouceApi,
    private val authDataSource: AuthDataSource,
    private val mapper: EditProfileAboutCloudMapper
) : EditProfileAboutRepository {
    override suspend fun send(value: String): Result<Unit> =
        authDataSource.user().flatMapCallback {
            authDataSource.updateUser(mapper.send(it, value))
        }

    override suspend fun fetch(): Result<String> =
        authDataSource.user().mapCatching { mapper.fetch(it) }
}
