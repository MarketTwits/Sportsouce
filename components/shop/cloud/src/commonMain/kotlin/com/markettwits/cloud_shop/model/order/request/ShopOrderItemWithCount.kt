package com.markettwits.cloud_shop.model.order.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShopOrderItemWithCount(
    @SerialName("product")
    val product: ShopOrderItemWithPrice,
    @SerialName("count")
    val count : Int
)