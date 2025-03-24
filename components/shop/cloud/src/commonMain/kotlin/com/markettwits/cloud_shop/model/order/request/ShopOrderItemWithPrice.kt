package com.markettwits.cloud_shop.model.order.request

import com.markettwits.cloud_shop.model.product.Product
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