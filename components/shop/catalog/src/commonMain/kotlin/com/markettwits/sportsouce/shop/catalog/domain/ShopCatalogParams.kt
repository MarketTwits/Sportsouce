package com.markettwits.sportsouce.shop.catalog.domain

sealed interface ShopCatalogParams {

    data class WithFilter(
        val categoryId: Int? = null,
        val options: List<String>? = null,
        val maxPrice: Int? = null,
        val minPrice : Int? = null,
    ) : ShopCatalogParams

    data class WithQuery(
        val query: String
    ) : ShopCatalogParams

}