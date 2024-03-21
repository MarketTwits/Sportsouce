package com.markettwits.cloud_shop.model.product

import kotlinx.serialization.Serializable

@Serializable
data class ColorTaste(
    val colorTaste: String,
    val product: Product
)