package com.markettwits.cloud_shop.model.product

import kotlinx.serialization.Serializable

@Serializable
data class Size(
    val product: Product,
    val size: String,
    val sizeUuid: String
)