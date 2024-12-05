package com.markettwits.start.register.presentation.registration.data.mapper

import com.markettwits.start.register.domain.StartRegistryResult
import com.markettwits.start.register.presentation.registration.domain.models.StartRegistrationResult
import com.markettwits.start_cloud.model.register.on_start.StartRegistrationResponse

class StartRegisterResultMapper {

    fun mapResult(
        response : StartRegistrationResponse
    ) : StartRegistrationResult {
        val payment =
            if (response.payment is StartRegistrationResponse.Payment.PaymentBase)
                (response.payment as StartRegistrationResponse.Payment.PaymentBase).formUrl else ""
        return StartRegistrationResult(
            isSuccess = true,
            isError = false,
            paymentUrl = payment
        )
    }
}