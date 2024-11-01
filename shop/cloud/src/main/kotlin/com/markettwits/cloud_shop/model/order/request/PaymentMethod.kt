package com.markettwits.cloud_shop.model.order.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PaymentMethod(s: String) {

    @SerialName("online")
    ONLINE("online"),

    @SerialName("on_receipt")
    ON_RECEIPT("on_receipt")

}