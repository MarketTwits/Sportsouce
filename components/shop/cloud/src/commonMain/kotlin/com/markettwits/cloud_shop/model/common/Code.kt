package com.markettwits.cloud_shop.model.common

import kotlinx.serialization.Serializable

@Serializable
data class Code(
    val code: String,
    val code_type: String,
    val id: String,
    val product_id: String
)