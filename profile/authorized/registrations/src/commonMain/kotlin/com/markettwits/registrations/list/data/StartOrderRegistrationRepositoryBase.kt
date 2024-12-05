package com.markettwits.registrations.list.data

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.cloud.model.start_registration.StartRegistrationResponse
import com.markettwits.core_ui.items.base_extensions.retryRunCatchingAsync
import com.markettwits.profile.api.AuthDataSource
import com.markettwits.registrations.list.data.mapper.UserRegistrationsMapper
import com.markettwits.registrations.list.domain.StartOrderInfo

class StartOrderRegistrationRepositoryBase(
    private val service: SportsouceApi,
    private val auth: AuthDataSource,
    private val mapper: UserRegistrationsMapper
) : StartOrderRegistrationRepository {

    override suspend fun registrations(): Result<List<StartOrderInfo>> = runCatching {
        val token = auth.updateToken()
        val user = auth.auth()
        val result = service.userRegistriesNew2(user.getOrThrow().id, token.getOrThrow())
        mapper.map(result)
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