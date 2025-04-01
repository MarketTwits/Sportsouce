package com.markettwits.sportsouce.start.cloud.model.start.fields

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Distance(
    @SerialName("combo")
    val combo: List<Int>? = null,
    @SerialName("id")
    val id: Int,
    @SerialName("order_number")
    val orderNumber: Int
)