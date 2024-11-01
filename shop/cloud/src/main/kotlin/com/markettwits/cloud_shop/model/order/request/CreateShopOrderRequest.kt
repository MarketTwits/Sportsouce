package com.markettwits.cloud_shop.model.order.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateShopOrderRequest(
    val userId : Int,
    val items : List<ShopOrderItem>,
    val paymentMethod : PaymentMethod,
    val deliveryMethod: DeliveryMethod
)