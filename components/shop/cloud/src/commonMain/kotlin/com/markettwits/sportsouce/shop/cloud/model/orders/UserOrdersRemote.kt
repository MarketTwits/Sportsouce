package com.markettwits.sportsouce.shop.cloud.model.orders

import com.markettwits.sportsouce.shop.cloud.model.order.common.Order
import kotlinx.serialization.Serializable

@Serializable
internal data class UserOrdersRemote(
    val count: Int,
    val rows: List<Order>
)