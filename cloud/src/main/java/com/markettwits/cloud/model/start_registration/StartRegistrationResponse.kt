package com.markettwits.cloud.model.start_registration

import kotlinx.serialization.Serializable

@Serializable
data class StartRegistrationResponse(
    val payment: Payment?,
    val success: Boolean
){
    @Serializable
    data class Payment(
        val formUrl: String,
        val orderId: String
    )
}

@Serializable
data class StartRegistrationResponseWithoutPayment(
    val success: Boolean
)