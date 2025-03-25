package com.markettwits.sportsouce.shop.item.domain.models

import kotlinx.serialization.Serializable

@Serializable
    data class ShopExtraOptions(val title: String, val items: List<OptionValue>) {

        @Serializable
        data class OptionValue(
            val id: String,
            val optionValue: String,
            val isProductsValue: Boolean,
        )
    }