package com.markettwits.edit_profile.edit_profile_change_password.data

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.cloud.model.change_password.ChangePasswordRequest
import com.markettwits.profile.api.AuthDataSource

class ChangePasswordDataSourceBase(
    private val service: SportsouceApi,
    private val auth: AuthDataSource,
) : ChangePasswordDataSource {

    override suspend fun changePassword(password: String, newPassword: String): Result<String> =
        runCatching {
            val token = auth.updateToken()
            val it = service.changePassword(
                ChangePasswordRequest(password, newPassword),
                token.getOrThrow()
            )
            auth.updatePassword(newPassword)
            it.message
        }
}