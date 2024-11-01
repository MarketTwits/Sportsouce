package com.markettwits.cloud_shop.model.order.response

import kotlinx.serialization.Serializable

@Serializable
data class CreateShopOrderResponse(
    val errors: List<Error>,
    val order: Order? = null,
    val payment: Payment? = null
){

    @Serializable
    data class Payment(
        val formUrl: String,
        val orderId: String
    )

    @Serializable
    data class Order(
        val cost: String,
        val createdAt: String,
        val externalId: String,
        val id: Int,
        val internalId: String,
        val paid_online: Boolean,
        val payment_method: String,
        val shipping_method: String,
        val updatedAt: String,
        val userId: Int
    )

    @Serializable
    data class Error(
        val existingQuantity: Int,
        val message: String,
        val partlyExists: Boolean,
        val productId: String,
        val requestedQuantity: Int
    )
}