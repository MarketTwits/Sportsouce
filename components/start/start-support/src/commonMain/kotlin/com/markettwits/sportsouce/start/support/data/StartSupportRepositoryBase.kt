package com.markettwits.sportsouce.start.support.data

import com.markettwits.sportsouce.start.cloud.api.start.SportSauceStartApi
import com.markettwits.sportsouce.start.support.data.mapper.StartSupportMapper
import com.markettwits.sportsouce.auth.service.api.AuthDataSource
import com.markettwits.sportsouce.auth.cloud.model.common.AuthException
import com.markettwits.sportsouce.profile.cloud.SportSauceNetworkProfileApi
import com.markettwits.sportsouce.profile.cloud.model.start_registration.StartRegistrationResponse
import com.markettwits.sportsouce.start.support.domain.StartDonationCheck
import com.markettwits.sportsouce.start.support.domain.SupportAuthorizationRequired
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CancellationException

internal class StartSupportRepositoryBase(
    private val service: SportSauceStartApi,
    private val mapper: StartSupportMapper,
    private val auth: AuthDataSource,
    private val profileApi: SportSauceNetworkProfileApi,
) : StartSupportRepository {
    override suspend fun donation(startId: Int, price: Int): Result<String> = request {
        service.donation(mapper.map(startId, price)).url
    }

    override suspend fun checkDonation(startId: Int): Result<StartDonationCheck> = request {
        val token = auth.updateToken().getOrThrow().takeIf { it.isNotBlank() }
            ?: throw SupportAuthorizationRequired()
        val result = service.checkDonation(startId, token)
        StartDonationCheck(result.risk, result.registration?.let {
            StartDonationCheck.Registration(it.groupId, it.paymentStatus, it.amountKopecks, it.canPay)
        }, result.matchedAmountKopecks)
    }

    override suspend fun payRegistration(groupId: Int): Result<String> = request {
        val result = profileApi.repay(groupId, auth.updateToken().getOrThrow())
        check(result.success) { "Не удалось перейти к оплате регистрации" }
        val url = (result.payment as? StartRegistrationResponse.Payment.PaymentBase)?.formUrl
        check(!url.isNullOrBlank()) { "Ссылка на оплату недоступна. Проверьте регистрацию в профиле." }
        url
    }

    private suspend fun <T> request(block: suspend () -> T): Result<T> = try {
        Result.success(block())
    } catch (cancelled: CancellationException) {
        throw cancelled
    } catch (error: Exception) {
        Result.failure(if (error is AuthException ||
            (error is ClientRequestException && error.response.status == HttpStatusCode.Unauthorized)) {
            SupportAuthorizationRequired()
        } else error)
    }
}
