package com.markettwits.cloud_shop.model.order.response

import com.markettwits.cloud_shop.model.order.common.Error
import com.markettwits.cloud_shop.model.order.common.Order
import com.markettwits.cloud_shop.model.order.common.Payment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateShopOrderResponse(
    @SerialName("errors") val errors: List<Error>,
    @SerialName("order") val order: Order? = null,
    @SerialName("payment") val payment: Payment? = null
)