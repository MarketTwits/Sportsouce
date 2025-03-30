package com.markettwits.sportsouce.edit_profile.edit_profile_about.data


import com.markettwits.core_ui.items.extensions.flatMapCallback
import com.markettwits.sportsouce.auth.service.api.AuthDataSource

class EditProfileAboutRepositoryBase(
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
