package com.markettwits.sportsouce.profile.cloud.model.start_price

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StartPriceResponse(
    @SerialName("additionalFieldsPrice")
    val additionalFieldsPrice: Int,
    @SerialName("isPaymentRequired")
    val isPaymentRequired: Boolean,
    @SerialName("priceWithoutDiscount")
    val priceWithoutDiscount: Int,
    @SerialName("totalPrice")
    val totalPrice: Int
)