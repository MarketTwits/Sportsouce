package com.markettwits.sportsouce.profile.cloud.model.start_price

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StartPriceRequest(
    @SerialName("id")
    val id: Int,
    @SerialName("nameList")
    val nameList: List<String>,
    @SerialName("start_id")
    val startId: Int
)