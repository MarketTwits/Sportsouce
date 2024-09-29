package com.markettwits.shop.catalog.domain.models

data class ShopItem(
    val id: String,
    val currentPrice: String,
    val previousPrice: String?,
    val discount: Int?,
    val title: String,
    val imageUrl: String,
)


data class ShopCatalogOptions(
    val catalogId: Int?,
)