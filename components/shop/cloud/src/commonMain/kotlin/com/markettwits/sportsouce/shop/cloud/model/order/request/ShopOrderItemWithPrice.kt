package com.markettwits.sportsouce.shop.cloud.model.order.request

import com.markettwits.sportsouce.shop.cloud.model.product.Product
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShopOrderItemWithPrice(
    @SerialName("price")
    val price : Int,
    @SerialName("product")
    val product: Product,
    @SerialName("product_id")
    val productId : String
)