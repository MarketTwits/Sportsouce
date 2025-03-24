package com.markettwits.cloud_shop.model.orders

import com.markettwits.cloud_shop.model.common.Image
import kotlinx.serialization.Serializable

@Serializable
data class OrderItemCompact(
    val brand: String,
    val code: String,
    val description: String,
    val discountPrice: Int,
    val id: String,
    val images: List<Image>,
    val model: String,
    val name: String,
    val orderProduct: OrderProductCostWithCount,
    val price: Int,
)