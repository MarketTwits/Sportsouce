package com.markettwits.sportsouce.shop.cloud.model.order.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Error(
    @SerialName("existingQuantity") val existingQuantity: Int,
    @SerialName("message") val message: String,
    @SerialName("partlyExists") val partlyExists: Boolean,
    @SerialName("productId") val productId: String,
    @SerialName("requestedQuantity") val requestedQuantity: Int
)