package com.markettwits.sportsouce.shop.cloud.model.product

import kotlinx.serialization.Serializable

@Serializable
data class Size(
    val product: Product,
    val size: String,
    val sizeUuid: String
)