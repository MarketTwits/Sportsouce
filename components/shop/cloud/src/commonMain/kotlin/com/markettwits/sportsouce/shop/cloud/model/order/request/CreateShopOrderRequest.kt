package com.markettwits.sportsouce.shop.cloud.model.order.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateShopOrderRequest(
    @SerialName("userId")
    val userId : Int,
    @SerialName("items")
    val items : List<ShopOrderItemWithCount>,
    @SerialName("paymentMethod")
    val paymentMethod : PaymentMethod,
    @SerialName("shipping_method")
    val deliveryMethod: DeliveryMethod
)