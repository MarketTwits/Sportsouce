package com.markettwits.shop.catalog.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class ShopFilterPrice(
    val minPrice: Value,
    val maxPrice: Value,
) {

    companion object {
        val EMPTY = ShopFilterPrice(Value.Empty, Value.Empty)
    }

    @Serializable
    sealed interface Value {
        data class Price(val cost: Int) : Value
        data object Empty : Value
    }

}