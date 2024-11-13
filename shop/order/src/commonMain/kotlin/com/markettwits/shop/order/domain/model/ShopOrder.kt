package com.markettwits.shop.order.domain.model

data class ShopOrder(
    val cost: String,
    val externalId: String? = null,
    val id: Int,
    val internalId: String,
    val paidOnline: Boolean,
    val createAt : String,
    val paymentMethod: ShopPaymentType,
    val shippingMethod: ShopDeliveryType,
)