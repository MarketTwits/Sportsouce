package com.markettwits.shop.orders.domain.models

import com.markettwits.shop.order.domain.model.ShopOrder

data class ShopUserOrder(
    val order : ShopOrder,
    val items : List<ShopItemOrder>
)