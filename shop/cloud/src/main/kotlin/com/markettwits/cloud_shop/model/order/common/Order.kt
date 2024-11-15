package com.markettwits.cloud_shop.model.order.common

import com.markettwits.cloud_shop.model.orders.OrderItemCompact
import com.markettwits.cloud_shop.model.orders.OrderStatus
import com.markettwits.cloud_shop.model.orders.UserRecipient
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Order(
    @SerialName("cost") val cost: String,
    @SerialName("createdAt") val createdAt: String,
    @SerialName("externalId") val externalId: String? = null,
    @SerialName("id") val id: Int,
    @SerialName("internalId") val internalId: String,
    @SerialName("paid_online") val paidOnline: Boolean,
    @SerialName("payment_method") val paymentMethod: String,
    @SerialName("shipping_method") val shippingMethod: String,
    @SerialName("orderItems") val orderItems : List<OrderItemCompact>? = null,
    @SerialName("status") val status : OrderStatus? = null,
    @SerialName("updatedAt") val updatedAt: String,
    @SerialName("userId") val userId: Int,
    @SerialName("user") val user: UserRecipient? = null,
)