package com.markettwits.shop.item.domain.models

import com.markettwits.shop.domain.model.ShopItem
import kotlinx.serialization.Serializable

@Serializable
data class ShopPageItem(
    val product : ShopItem,
    val extraOptions: List<ExtraOption>,
) {
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

