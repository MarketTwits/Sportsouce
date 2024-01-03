package com.markettwits.change_password.data

import com.markettwits.cloud.model.auth.common.AuthErrorResponse
import com.markettwits.cloud.model.auth.common.AuthException
import com.markettwits.cloud.model.change_password.ChangePasswordRequest
import com.markettwits.profile.data.AuthDataSource
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.alexpanov.core_network.api.SportsouceApi

class ChangePasswordDataSourceBase(
    private val service: SportsouceApi,
    private val auth: AuthDataSource
) : ChangePasswordDataSource {
    override val state: MutableStateFlow<String> = MutableStateFlow("")
    override suspend fun changePassword(password: String, newPassword: String) {
        runCatching<String> {
            val token = auth.updateToken()
            service.changePassword(ChangePasswordRequest(password, newPassword), token)
        }.onFailure {
            val message = when (it) {
                is ClientRequestException -> it.response.body<AuthErrorResponse>().message
                is AuthException -> it.exception
                else -> it.toString()
            }
            state.value = message
        }.onSuccess {
            state.value = it
        }
    }

    override suspend fun changePasswordNew(password: String, newPassword: String): Result<String> {
        return try {
            val token = auth.updateToken()
            val it = service.changePassword(ChangePasswordRequest(password, newPassword), token)
            Result.success(it)
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