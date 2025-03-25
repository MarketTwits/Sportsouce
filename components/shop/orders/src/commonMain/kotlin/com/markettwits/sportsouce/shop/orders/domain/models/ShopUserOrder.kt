package com.markettwits.sportsouce.shop.orders.domain.models

import com.markettwits.sportsouce.shop.order.domain.model.ShopOrder

data class ShopUserOrder(
    val order : ShopOrder,
    val items : List<ShopUserItemOrder>
)