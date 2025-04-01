package com.markettwits.sportsouce.start.cloud.model.start.fields

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Discount(
    @SerialName("c_from")
    val cFrom: Int,
    @SerialName("c_to")
    val cTo: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("start_id")
    val startId: Int,
    @SerialName("percent")
    val percent: Boolean?,
    @SerialName("type")
    val type: String?,
    @SerialName("value")
    val value: Int
)