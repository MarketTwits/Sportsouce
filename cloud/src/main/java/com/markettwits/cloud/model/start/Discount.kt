package com.markettwits.cloud.model.start

import kotlinx.serialization.Serializable

@Serializable
data class Discount(
    val c_from: Int,
    val c_to: Int,
    val id: Int,
    val start_id: Int,
    val type: String?,
    val value: Int
)