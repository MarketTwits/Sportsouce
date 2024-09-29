package com.markettwits.shop.item.domain.models

data class ShopPageItem(
    val id: String,
    val code: String,
    val fullPathUrl: String,
    val price: Price,
    val visual: Visual,
    val options: List<Option>,
    val extraOptions: List<ExtraOption>,
) {
    data class Price(
        val currentPrice: String,
        val previousPrice: String?,
        val discount: Int?,
    )

    data class Visual(
        val imageUrl: String,
        val description: String,
        val displayName: String,
    )

    data class Option(val id: String, val optionTitle: String, val optionValue: String)

    data class ExtraOption(val title: String, val items: List<OptionValue>)

    data class OptionValue(val id: String, val optionValue: String, val isProductsValue: Boolean)


}

