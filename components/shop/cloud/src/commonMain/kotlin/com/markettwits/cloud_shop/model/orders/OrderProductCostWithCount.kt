package com.markettwits.cloud_shop.model.orders

import kotlinx.serialization.Serializable

@Serializable
data class OrderProductCostWithCount(
    val cost: String,
    val count: Int
)