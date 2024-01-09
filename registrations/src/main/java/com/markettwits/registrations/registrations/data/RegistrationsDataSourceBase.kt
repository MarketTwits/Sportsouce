package com.markettwits.registrations.registrations.data

import com.markettwits.cloud.model.auth.common.AuthErrorResponse
import com.markettwits.cloud.model.auth.common.AuthException
import com.markettwits.profile.data.AuthDataSource
import com.markettwits.registrations.registrations.presentation.RegistrationsStore
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import com.markettwits.cloud.api.SportsouceApi

class RegistrationsDataSourceBase(
    private val service: SportsouceApi,
    private val auth: AuthDataSource,
    private val mapper: RemoteRegistrationsToUiMapper
) : RegistrationsDataSource {
    override suspend fun registrations(): Result<RegistrationsStore.State> {
        return try {
            val token = auth.updateToken()
            val user = auth.auth()
            val result = service.userRegistries(user.id, token)
            Result.success(mapper.map(result))
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