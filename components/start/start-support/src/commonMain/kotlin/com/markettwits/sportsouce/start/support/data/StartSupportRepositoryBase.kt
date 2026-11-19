package com.markettwits.sportsouce.start.support.data

import com.markettwits.sportsouce.start.cloud.api.start.SportSauceStartApi
import com.markettwits.sportsouce.start.support.data.mapper.StartSupportMapper
import com.markettwits.sportsouce.auth.service.api.AuthDataSource
import com.markettwits.sportsouce.auth.cloud.model.common.AuthException
import com.markettwits.sportsouce.profile.cloud.SportSauceNetworkProfileApi
import com.markettwits.sportsouce.profile.cloud.model.registrations.UserRegistration
import com.markettwits.sportsouce.profile.cloud.model.start_registration.StartRegistrationResponse
import com.markettwits.sportsouce.profile.cloud.registrations.UserRegistrationsDataSource
import com.markettwits.sportsouce.start.support.domain.StartDonationCheck
import com.markettwits.sportsouce.start.support.domain.CachedStartRegistration
import com.markettwits.sportsouce.start.support.domain.SupportAuthorizationRequired
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CancellationException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

internal class StartSupportRepositoryBase(
    private val service: SportSauceStartApi,
    private val mapper: StartSupportMapper,
    private val auth: AuthDataSource,
    private val profileApi: SportSauceNetworkProfileApi,
    private val registrations: UserRegistrationsDataSource,
) : StartSupportRepository {
    override suspend fun donation(startId: Int, price: Int): Result<String> = request {
        service.donation(mapper.map(startId, price)).url
    }

    override suspend fun cachedRegistration(startId: Int): Result<CachedStartRegistration> = request {
        val accountId = auth.cachedUserId().getOrThrow()
        val check = registrations.cached(accountId)?.let {
            StartDonationCheck("none", null, null).withProfileRegistrations(startId, it)
                .takeIf(StartDonationCheck::hasRegistration)
        }
        CachedStartRegistration(accountId, check)
    }

    override suspend fun checkDonation(startId: Int, refreshRegistrations: Boolean): Result<StartDonationCheck> = request {
        val token = auth.updateToken().getOrThrow().takeIf { it.isNotBlank() }
            ?: throw SupportAuthorizationRequired()
        val result = service.checkDonation(startId, token)
        val check = StartDonationCheck(result.risk, result.registration?.let {
            StartDonationCheck.Registration(it.groupId, it.paymentStatus, it.amountKopecks, it.canPay)
        }, result.matchedAmountKopecks)
        if (check.registration != null || check.hasUnpaidRegistration) return@request check

        // The donation check can omit paid registrations. Keep its risk and payment data authoritative.
        val profileCheck = request {
            val userId = auth.cachedUserId().getOrThrow()
            val maxAge = if (refreshRegistrations) Duration.ZERO else 15.seconds
            check.withProfileRegistrations(startId, registrations.refresh(userId, token, maxAge))
        }
        profileCheck.getOrElse { error ->
            if (error is SupportAuthorizationRequired) throw error
            check.copy(isRegistrationKnown = false)
        }
    }

    override suspend fun payRegistration(groupId: Int): Result<String> = request {
        val result = profileApi.repay(groupId, auth.updateToken().getOrThrow())
        check(result.success) { "Не удалось перейти к оплате регистрации" }
        val url = (result.payment as? StartRegistrationResponse.Payment.PaymentBase)?.formUrl
        check(!url.isNullOrBlank()) { "Ссылка на оплату недоступна. Проверьте регистрацию в профиле." }
        url
    }

    private fun StartDonationCheck.withProfileRegistrations(startId: Int, orders: List<UserRegistration>): StartDonationCheck {
        val matching = orders.filter { (it.startId ?: it.start?.id) == startId && it.isRefunded != true }
        if (matching.any { it.payment == 1 || it.payment == 2 || it.payment == 4 }) {
            return copy(hasConfirmedRegistration = true)
        }
        val unpaid = matching.firstOrNull { it.payment == null || it.payment == 0 || it.payment == 3 } ?: return this
        return copy(registration = StartDonationCheck.Registration(
            groupId = unpaid.id ?: 0,
            paymentStatus = unpaid.payment ?: 0,
            amountKopecks = unpaid.price?.toLong(),
            canPay = false,
        ))
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
