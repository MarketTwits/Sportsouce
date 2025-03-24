package com.markettwits.cloud.model.start_price

import kotlinx.serialization.Serializable

@Serializable
data class StartPriceRequest(
    val id: Int,
    val nameList: List<String>,
    val start_id: Int
)