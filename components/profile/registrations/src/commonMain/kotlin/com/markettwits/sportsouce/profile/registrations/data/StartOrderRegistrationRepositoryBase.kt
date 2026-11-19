package com.markettwits.sportsouce.profile.registrations.data

import com.markettwits.core_cloud.provider.ServiceResponseException
import com.markettwits.sportsouce.auth.cloud.model.common.AuthException
import com.markettwits.sportsouce.auth.service.api.AuthDataSource
import com.markettwits.sportsouce.auth.service.api.SharedUser
import com.markettwits.sportsouce.profile.cloud.SportSauceNetworkProfileApi
import com.markettwits.sportsouce.profile.cloud.registrations.UserRegistrationsDataSource
import com.markettwits.sportsouce.profile.cloud.model.start_registration.StartRegistrationResponse
import com.markettwits.sportsouce.profile.registrations.data.mapper.UserRegistrationsMapper
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderPrice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.CancellationException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode

class StartOrderRegistrationRepositoryBase(
    private val service: SportSauceNetworkProfileApi,
    private val auth: AuthDataSource,
    private val mapper: UserRegistrationsMapper,
    private val registrationsDataSource: UserRegistrationsDataSource,
) : StartOrderRegistrationRepository {

    override fun registrations(forced: Boolean, allowCachedOnError: Boolean): Flow<List<StartOrderInfo>> = flow {
        val userId = auth.userID().getOrThrow()
        val cached = if (forced && !allowCachedOnError) null else registrationsDataSource.cached(userId)
        if (!forced && cached != null) emit(mapper.map(cached))

        val updated = try {
            val token = auth.updateToken().getOrThrow()
            registrationsDataSource.refresh(userId, token)
        } catch (error: Exception) {
            if (!allowCachedOnError || error is CancellationException || error is AuthException ||
                (error is ClientRequestException && error.response.status == HttpStatusCode.Unauthorized)
            ) throw error
            val lastSnapshot = registrationsDataSource.cached(userId) ?: throw error
            if (forced || lastSnapshot != cached) emit(mapper.map(lastSnapshot))
            return@flow
        }
        if (forced || updated != cached) emit(mapper.map(updated))
    }

    override suspend fun currentUser(): Result<SharedUser> = auth.sharedUser()

    override suspend fun pay(id: Int): Result<String> {
        return runCatching {
            val response = service.repay(id, auth.updateToken().getOrThrow())
            if (!response.success) throw ServiceResponseException("Не удалось перейти к оплате регистрации.")
            when (val payment = response.payment) {
                is StartRegistrationResponse.Payment.PaymentBase -> payment.formUrl
                is StartRegistrationResponse.Payment.PaymentString -> payment.payment.takeIf {
                    it.startsWith("https://") || it.startsWith("http://")
                }.orEmpty()
                null -> ""
            }
        }
    }

    override suspend fun getPrice(
        orderId: Int,
        orderDistancesId: List<String>,
        startId: Int
    ): Result<StartOrderPrice> = runCatching {
        val token = auth.updateToken().getOrThrow()
        val result = service.checkStartPrice(orderId, token)
        mapper.mapPrice(result)
    }
}
