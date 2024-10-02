package com.markettwits.shop.filter.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class ShopCategoryItem(
    val children: List<ShopCategoryItem>,
    val description: String,
    val id: Int,
    val level: Int,
    val parentId: Int,
    val slug: String,
    val title: String,
)