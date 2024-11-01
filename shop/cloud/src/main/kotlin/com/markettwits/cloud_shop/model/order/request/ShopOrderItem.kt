package com.markettwits.cloud_shop.model.order.request

import com.markettwits.cloud_shop.model.product.Product
import kotlinx.serialization.Serializable

@Serializable
data class ShopOrderItem(
    val product: Product,
    val count : Int
)