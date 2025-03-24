package com.markettwits.cloud_shop.model.product

import kotlinx.serialization.Serializable

@Serializable
data class Value(
    val createdAt: String,
    val imageId: Int?,
    val name: String,
    val product_option_uuid: String,
    val updatedAt: String,
    val uuid: String
)