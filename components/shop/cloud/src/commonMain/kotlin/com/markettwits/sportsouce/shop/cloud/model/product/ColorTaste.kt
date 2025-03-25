package com.markettwits.sportsouce.shop.cloud.model.product

import kotlinx.serialization.Serializable

@Serializable
data class ColorTaste(
    val colorTaste: String,
    val product: Product
)