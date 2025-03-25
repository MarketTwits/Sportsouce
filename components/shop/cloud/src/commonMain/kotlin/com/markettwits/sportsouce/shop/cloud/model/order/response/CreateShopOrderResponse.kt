package com.markettwits.sportsouce.shop.cloud.model.order.response

import com.markettwits.sportsouce.shop.cloud.model.order.common.Error
import com.markettwits.sportsouce.shop.cloud.model.order.common.Order
import com.markettwits.sportsouce.shop.cloud.model.order.common.Payment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateShopOrderResponse(
    @SerialName("errors") val errors: List<Error>,
    @SerialName("order") val order: Order? = null,
    @SerialName("payment") val payment: Payment? = null
)