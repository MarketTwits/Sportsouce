package com.markettwits.cloud_shop.model.filter

import kotlinx.serialization.Serializable

@Serializable
data class FilterValue(
    val createdAt: String,
    val imageId: Int?,
    val name: String,
    val product_option_uuid: String,
    val updatedAt: String,
    val uuid: String
)