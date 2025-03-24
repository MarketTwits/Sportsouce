package com.markettwits.shop.filter.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class ShopFilterResult(
    val categoryId: Int? = null,
    val options: List<String>,
    val maxPrice : Int? = null,
    val minPrice : Int? = null
)