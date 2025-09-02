package com.markettwits.sportsouce.profile.registrations.data

import com.markettwits.cahce.execute.base.ExecuteWithCache
import com.markettwits.core_ui.items.extensions.retryRunCatchingAsync
import com.markettwits.sportsouce.auth.service.api.AuthDataSource
import com.markettwits.sportsouce.auth.service.api.SharedUser
import com.markettwits.sportsouce.profile.cloud.SportSauceNetworkProfileApi
import com.markettwits.sportsouce.profile.cloud.model.registrations.UserRegistration
import com.markettwits.sportsouce.profile.cloud.model.start_price.StartPriceRequest
import com.markettwits.sportsouce.profile.cloud.model.start_registration.StartRegistrationResponse
import com.markettwits.sportsouce.profile.registrations.data.cache.UserStartsRegistrationsCache
import com.markettwits.sportsouce.profile.registrations.data.mapper.UserRegistrationsMapper
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderPrice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class StartOrderRegistrationRepositoryBase(
    private val service: SportSauceNetworkProfileApi,
    private val auth: AuthDataSource,
    private val mapper: UserRegistrationsMapper,
    private val cache: UserStartsRegistrationsCache,
    private val executeWithCache: ExecuteWithCache,
) : StartOrderRegistrationRepository {

    override fun registrations(forced: Boolean): Flow<List<StartOrderInfo>> = flow {
        executeWithCache.executeWithCache(
            forced = forced,
            cache = cache,
            launch = ::launchRaw,
            callback = { rawRegistrations ->
                emit(mapper.map(rawRegistrations))
            }
        )
    }

    private suspend fun launchRaw(): List<UserRegistration> {
        val token = auth.updateToken().getOrThrow()
        val user = auth.auth().getOrThrow()
        return service.userRegistries(user.id, token)
    }

    override suspend fun currentUser(): Result<SharedUser> = auth.sharedUser()

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
            id = orderId,
            token = token
        )
        mapper.mapPrice(result)
    }
}