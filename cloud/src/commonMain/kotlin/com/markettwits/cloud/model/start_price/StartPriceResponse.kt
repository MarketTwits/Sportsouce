package com.markettwits.cloud.model.start_price

import kotlinx.serialization.Serializable

@Serializable
data class StartPriceResponse(
    val additionalFieldsPrice: Int,
    val isPaymentRequired: Boolean,
    val priceWithoutDiscount: Int,
    val totalPrice: Int
)