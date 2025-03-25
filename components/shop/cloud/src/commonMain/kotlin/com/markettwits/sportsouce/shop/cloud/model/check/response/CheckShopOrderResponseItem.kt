package com.markettwits.sportsouce.shop.cloud.model.check.response

import kotlinx.serialization.Serializable

@Serializable
data class CheckShopOrderResponseItem(
    val messages: List<String>,
    val product_id: String,
    val success: Boolean
)