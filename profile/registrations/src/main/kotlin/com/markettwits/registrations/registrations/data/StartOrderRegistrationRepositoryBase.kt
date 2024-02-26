package com.markettwits.registrations.registrations.data

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.cloud.model.auth.common.AuthErrorResponse
import com.markettwits.cloud.model.auth.common.AuthException
import com.markettwits.cloud.model.start_registration.StartRegistrationResponse
import com.markettwits.core_ui.base_extensions.retryRunCatchingAsync
import com.markettwits.profile.data.AuthDataSource
import com.markettwits.registrations.registrations.data.mapper.UserRegistrationsMapper
import com.markettwits.registrations.registrations.domain.StartOrderInfo
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException

class StartOrderRegistrationRepositoryBase(
    private val service: SportsouceApi,
    private val auth: AuthDataSource,
    private val mapper: UserRegistrationsMapper
) : StartOrderRegistrationRepository {
    override suspend fun registrations(): Result<List<StartOrderInfo>> {
        return try {
            val token = auth.updateToken()
            val user = auth.auth()
            val result = service.userRegistries(user.getOrThrow().id, token.getOrThrow())
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
            val response = service.repay(id, token.getOrThrow())
            when (response.payment) {
                is StartRegistrationResponse.Payment.PaymentBase -> (response.payment as StartRegistrationResponse.Payment.PaymentBase).formUrl
                is StartRegistrationResponse.Payment.PaymentString -> ""
                else -> ""
            }
        }
    }
}