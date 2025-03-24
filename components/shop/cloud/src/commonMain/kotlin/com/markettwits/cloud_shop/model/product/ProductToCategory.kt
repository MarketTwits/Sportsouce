package com.markettwits.cloud_shop.model.product

import kotlinx.serialization.Serializable

@Serializable
data class ProductToCategory(
    val category_id: Int,
    val id: Int,
    val product_id: String
)