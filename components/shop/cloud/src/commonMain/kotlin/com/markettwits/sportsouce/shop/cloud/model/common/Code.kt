package com.markettwits.sportsouce.shop.cloud.model.common

import kotlinx.serialization.Serializable

@Serializable
data class Code(
    val code: String,
    val code_type: String,
    val id: String,
    val product_id: String
)