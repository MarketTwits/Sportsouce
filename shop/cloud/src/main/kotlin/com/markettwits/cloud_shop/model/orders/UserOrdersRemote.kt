package com.markettwits.cloud_shop.model.orders

import com.markettwits.cloud_shop.model.order.common.Order
import kotlinx.serialization.Serializable

@Serializable
internal data class UserOrdersRemote(
    val count: Int,
    val rows: List<Order>
)