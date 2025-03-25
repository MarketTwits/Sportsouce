package com.markettwits.sportsouce.start.cloud.model.filters

import kotlinx.serialization.Serializable

@Serializable
data class Gender(
    val count: Int,
    val value: String
)