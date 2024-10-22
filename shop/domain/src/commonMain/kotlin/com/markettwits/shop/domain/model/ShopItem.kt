package com.markettwits.shop.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class ShopItem(
    val id: String,
    val code: String,
    val quantity : Int,
    val fullPathUrl: String,
    val price: Price,
    val visual: Visual,
    val options: List<Option>,
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
}
