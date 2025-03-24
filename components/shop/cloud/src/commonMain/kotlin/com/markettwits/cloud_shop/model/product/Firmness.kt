package com.markettwits.cloud_shop.model.product

import kotlinx.serialization.Serializable

@Serializable
data class Firmness(
    val firmness: String,
    val firmnessUuid: String,
    val product: Product
)