package com.markettwits.registrations.registrations.data

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.cloud.model.auth.common.AuthErrorResponse
import com.markettwits.cloud.model.auth.common.AuthException
import com.markettwits.cloud.model.start_registration.StartRegistrationResponse
import com.markettwits.core_ui.base_extensions.retryRunCatchingAsync
import com.markettwits.profile.data.AuthDataSource
import com.markettwits.registrations.registrations.presentation.RegistrationsStore
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException

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

    override suspend fun pay(id: Int): Result<String> {
        return retryRunCatchingAsync {
            val token = auth.updateToken()
            val response = service.repay(id, token)
            when (response.payment) {
                is StartRegistrationResponse.Payment.PaymentBase -> (response.payment as StartRegistrationResponse.Payment.PaymentBase).formUrl
                is StartRegistrationResponse.Payment.PaymentString -> ""
                else -> ""
            }
        }
    }
}