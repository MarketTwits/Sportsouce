package com.markettwits.start_cloud.model.start.fields

import kotlinx.serialization.Serializable

@Serializable
data class Distance(
    val combo: List<Int>? = null,
    val id: Int,
    val order_number: Int
)