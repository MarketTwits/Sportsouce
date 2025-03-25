package com.markettwits.sportsouce.start.register.presentation.registration.common.data.mapper

import com.markettwits.sportsouce.start.cloud.model.register.on_start.StartRegistrationResponse
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationResult

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