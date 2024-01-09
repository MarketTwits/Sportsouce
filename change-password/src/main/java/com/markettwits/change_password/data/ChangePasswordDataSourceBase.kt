package com.markettwits.change_password.data

import com.markettwits.cloud.model.auth.common.AuthErrorResponse
import com.markettwits.cloud.model.auth.common.AuthException
import com.markettwits.cloud.model.change_password.ChangePasswordRequest
import com.markettwits.profile.data.AuthDataSource
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import com.markettwits.cloud.api.SportsouceApi

class ChangePasswordDataSourceBase(
    private val service: SportsouceApi,
    private val auth: AuthDataSource,
) : ChangePasswordDataSource {

    override suspend fun changePassword(password: String, newPassword: String): Result<String> {
        return try {
            val token = auth.updateToken()
            val it = service.changePassword(ChangePasswordRequest(password, newPassword), token)
            auth.updatePassword(newPassword)
            Result.success(it.message)
        } catch (e: Exception) {
            val message = when (e) {
                is ClientRequestException -> e.response.body<AuthErrorResponse>().message
                is AuthException -> e.exception
                else -> e.message
            }
            Result.failure(Exception(message))
        }
    }
}