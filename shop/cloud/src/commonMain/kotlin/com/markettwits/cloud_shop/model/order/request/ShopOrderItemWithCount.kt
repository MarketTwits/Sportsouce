package com.markettwits.cloud_shop.model.order.request

import com.markettwits.cloud_shop.model.product.Product
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShopOrderItemWithCount(
    @SerialName("product")
    val product: ShopOrderItemWithPrice,
    @SerialName("count")
    val count : Int
)