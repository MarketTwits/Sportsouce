package com.markettwits.sportsouce.profile.registrations.list.data

import com.markettwits.core_ui.items.extensions.retryRunCatchingAsync
import com.markettwits.sportsouce.auth.service.api.AuthDataSource
import com.markettwits.sportsouce.profile.cloud.SportSauceNetworkProfileApi
import com.markettwits.sportsouce.profile.cloud.model.start_price.StartPriceRequest
import com.markettwits.sportsouce.profile.cloud.model.start_registration.StartRegistrationResponse
import com.markettwits.sportsouce.profile.registrations.list.data.mapper.UserRegistrationsMapper
import com.markettwits.sportsouce.profile.registrations.list.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.list.domain.StartOrderPrice

class StartOrderRegistrationRepositoryBase(
    private val service: SportSauceNetworkProfileApi,
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

    override suspend fun getPrice(
        orderId: Int,
        orderDistancesId: List<String>,
        startId: Int
    ): Result<StartOrderPrice> = runCatching {
        val token = auth.updateToken().getOrThrow()
        val result = service.checkStartPrice(
            startPriceRequest = StartPriceRequest(orderId, orderDistancesId, startId),
            orderId,
            token
        )
        mapper.mapPrice(result)
    }
}