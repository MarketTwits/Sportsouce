package com.markettwits.start.register.data.registration.mapper

import com.markettwits.cloud.model.auth.common.AuthErrorResponse
import com.markettwits.cloud.model.start_registration.StartRegistrationResponse
import com.markettwits.start.register.domain.StartRegistryResult
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException

interface RegistrationResponseMapper {
    suspend fun flatMap(response: Result<StartRegistrationResponse>): StartRegistryResult
    fun map(response: StartRegistrationResponse): StartRegistryResult
    suspend fun map(response: Throwable): StartRegistryResult
}

class RegistrationResponseMapperBase : RegistrationResponseMapper {
    override suspend fun flatMap(response: Result<StartRegistrationResponse>): StartRegistryResult {
        return response.fold(
            onSuccess = { map(it) },
            onFailure = { map(it) }
        )
    }

    override fun map(response: StartRegistrationResponse): StartRegistryResult {
        val payment =
            if (response.payment is StartRegistrationResponse.Payment.PaymentBase)
                (response.payment as StartRegistrationResponse.Payment.PaymentBase).formUrl else ""
        return StartRegistryResult(
            isSuccess = true,
            isError = false,
            message = "Вы успешо зарегестрировались, оплатите в личном кабинете",
            paymentUrl = payment
        )
    }

    override suspend fun map(response: Throwable): StartRegistryResult {
        if (response is ClientRequestException) {
            return StartRegistryResult(
                isSuccess = false,
                isError = true,
                message = response.response.body<AuthErrorResponse>().message,
                paymentUrl = ""
            )
        } else {
            return StartRegistryResult(
                isSuccess = false,
                isError = true,
                message = response.message.toString(),
                paymentUrl = ""
            )
        }
    }
}