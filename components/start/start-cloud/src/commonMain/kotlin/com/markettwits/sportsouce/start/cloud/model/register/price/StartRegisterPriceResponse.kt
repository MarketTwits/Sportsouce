package com.markettwits.sportsouce.start.cloud.model.register.price

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StartRegisterPriceResponse(
    @SerialName("additionalFieldsPrice")
    val additionalFieldsPrice: Int,
    @SerialName("isPaymentRequired")
    val isPaymentRequired: Boolean,
    @SerialName("priceWithoutDiscount")
    val priceWithoutDiscount: Int,
    @SerialName("totalPrice")
    val totalPrice: Int
)