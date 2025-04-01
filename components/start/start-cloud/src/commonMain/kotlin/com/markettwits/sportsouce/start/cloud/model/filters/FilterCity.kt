package com.markettwits.sportsouce.start.cloud.model.filters

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FilterCity(
    @SerialName("count")
    val count: Int,
    @SerialName("value")
    val value: String
)