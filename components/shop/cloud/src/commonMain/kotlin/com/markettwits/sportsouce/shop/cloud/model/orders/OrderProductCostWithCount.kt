package com.markettwits.sportsouce.shop.cloud.model.orders

import kotlinx.serialization.Serializable

@Serializable
data class OrderProductCostWithCount(
    val cost: String,
    val count: Int
)