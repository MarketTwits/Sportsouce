package com.markettwits.sportsouce.edit_profile.edit_profile_change_password.data

import com.markettwits.sportsouce.auth.service.api.AuthDataSource
import com.markettwits.sportsouce.profile.cloud.SportSauceNetworkProfileApi
import com.markettwits.sportsouce.profile.cloud.model.update.ChangePasswordRequest

class ChangePasswordDataSourceBase(
    private val service: SportSauceNetworkProfileApi,
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