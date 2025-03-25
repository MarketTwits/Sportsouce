package com.markettwits.sportsouce.shop.cloud.model.order.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShopOrderItemWithCount(
    @SerialName("product")
    val product: ShopOrderItemWithPrice,
    @SerialName("count")
    val count : Int
)