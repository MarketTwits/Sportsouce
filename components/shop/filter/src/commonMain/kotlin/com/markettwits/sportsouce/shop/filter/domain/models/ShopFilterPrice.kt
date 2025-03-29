package com.markettwits.sportsouce.shop.filter.domain.models

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

        fun apply() : Int?

        data class Price(val cost: Int) : Value {
            override fun apply(): Int = cost
        }

        data object Empty : Value {
            override fun apply(): Int? = null
        }
    }
}