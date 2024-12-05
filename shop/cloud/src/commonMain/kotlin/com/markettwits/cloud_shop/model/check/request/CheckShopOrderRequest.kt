package com.markettwits.cloud_shop.model.check.request

import com.markettwits.cloud_shop.model.order.request.ShopOrderItemWithCount
import kotlinx.serialization.Serializable

@Serializable
data class CheckShopOrderRequest(
    val items : List<ShopOrderItemWithCount>,
)