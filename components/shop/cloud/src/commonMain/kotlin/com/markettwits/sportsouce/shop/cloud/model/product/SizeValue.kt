package com.markettwits.sportsouce.shop.cloud.model.product

import kotlinx.serialization.Serializable

@Serializable
data class SizeValue(
    val createdAt: String,
    val imageId: Int?,
    val name: String,
    val product_option_uuid: String,
    val updatedAt: String,
    val uuid: String
)