package com.markettwits.shop.item.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class ShopPageItem(
    val id: String,
    val code: String,
    val quantity : Int,
    val fullPathUrl: String,
    val price: Price,
    val visual: Visual,
    val options: List<Option>,
    val extraOptions: List<ExtraOption>,
) {
    @Serializable
    data class Price(
        val currentPrice: String,
        val previousPrice: String?,
        val discount: Int?,
    )

    @Serializable
    data class Visual(
        val imageUrl: List<String>,
        val description: String,
        val displayName: String,
    )

    @Serializable
    data class Option(val id: String, val optionTitle: String, val optionValue: String)

    @Serializable
    data class ExtraOption(val title: String, val items: List<OptionValue>) {
        @Serializable
        data class OptionValue(
            val id: String,
            val optionValue: String,
            val isProductsValue: Boolean,
        )
    }


}

