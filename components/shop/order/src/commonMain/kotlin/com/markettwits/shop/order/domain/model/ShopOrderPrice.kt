package com.markettwits.shop.order.domain.model

data class ShopOrderPrice(
    val totalPrice : Int,
    val totalDiscount : Int,
    val productCount : Int
)