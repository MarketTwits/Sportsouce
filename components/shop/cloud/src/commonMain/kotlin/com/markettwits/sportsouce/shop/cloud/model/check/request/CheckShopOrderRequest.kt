package com.markettwits.sportsouce.shop.cloud.model.check.request

import com.markettwits.sportsouce.shop.cloud.model.order.request.ShopOrderItemWithCount
import kotlinx.serialization.Serializable

@Serializable
data class CheckShopOrderRequest(
    val items : List<ShopOrderItemWithCount>,
)