package com.markettwits.start_cloud.model.register.price

import kotlinx.serialization.Serializable

@Serializable
data class StartRegisterPriceResponse(
    val additionalFieldsPrice: Int,
    val isPaymentRequired: Boolean,
    val priceWithoutDiscount: Int,
    val totalPrice: Int
)