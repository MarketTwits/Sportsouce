package com.markettwits.cloud_shop.model.product

import kotlinx.serialization.Serializable

@Serializable
data class Gender(
    val gender: String,
    val product: Product
)