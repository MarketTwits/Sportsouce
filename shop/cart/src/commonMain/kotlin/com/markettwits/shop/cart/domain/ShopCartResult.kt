package com.markettwits.shop.cart.domain

sealed interface ShopCartResult {

    fun isAvailable() : Boolean

    data object Available : ShopCartResult {
        override fun isAvailable() : Boolean = true
    }

    data object UnAvailable : ShopCartResult {
        override fun isAvailable(): Boolean = false
    }
}