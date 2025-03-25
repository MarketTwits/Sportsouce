package com.markettwits.sportsouce.shop.cloud.model.orders

import kotlinx.serialization.SerialName

enum class OrderStatus {
    @SerialName("confirmed") CONFIRMED,
    @SerialName("completed") COMPLETED,
    @SerialName("pending") PENDING,
    @SerialName("cancelled") CANCELLED
}