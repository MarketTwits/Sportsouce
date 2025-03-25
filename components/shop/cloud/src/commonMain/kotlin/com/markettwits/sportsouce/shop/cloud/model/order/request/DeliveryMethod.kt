package com.markettwits.sportsouce.shop.cloud.model.order.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class DeliveryMethod(s : String) {

    @SerialName("delivery")
    DELIVERY("delivery"),

    @SerialName("pickup")
    PICKUP("pickup")
}