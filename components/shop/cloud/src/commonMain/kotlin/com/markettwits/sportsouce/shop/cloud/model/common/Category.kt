package com.markettwits.sportsouce.shop.cloud.model.common

import com.markettwits.sportsouce.shop.cloud.model.product.ProductToCategory
import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val ProductToCategory: ProductToCategory,
    val description: String,
    val has_child: Int,
    val id: Int,
    val level: Int,
    val parent_id: Int?,
    val slug: String,
    val title: String,
)