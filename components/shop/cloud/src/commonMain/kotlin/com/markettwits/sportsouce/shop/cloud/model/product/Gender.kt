package com.markettwits.sportsouce.shop.cloud.model.product

import kotlinx.serialization.Serializable

@Serializable
data class Gender(
    val gender: String,
    val product: Product
)