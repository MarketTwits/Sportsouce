package com.markettwits.sportsouce.shop.order.domain.model

import com.markettwits.sportsouce.shop.order.domain.model.ShopOrderResult.Companion.BASE_ORDER_PAGE_URL

data class ShopOrder(
    val cost: String,
    val status : ShopOrderStatus,
    val externalId: String? = null,
    val id: Int,
    val internalId: String,
    val paidOnline: Boolean,
    val createAt : String,
    val paymentMethod: ShopPaymentType,
    val shippingMethod: ShopDeliveryType,
)

fun ShopOrder.getOrderDetailUrl() : String =
    "$BASE_ORDER_PAGE_URL=$internalId"
