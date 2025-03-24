package com.markettwits.cloud_shop.model.order.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Payment(
    @SerialName("formUrl") val formUrl: String,
    @SerialName("orderId") val orderId: String
)