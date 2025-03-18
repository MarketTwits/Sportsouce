package com.markettwits.shop.cart.domain

import com.markettwits.shop.domain.model.ShopItem
import kotlinx.serialization.Serializable

@Serializable
data class ShopItemCart(
    val item : ShopItem,
    val count: Int = 0,
){
    val numberPrice = item.price.currentPrice.replace(Regex("\\s+"), "").toIntOrNull() ?: 0

    val numberPreviousPrice : Int? = item.price.previousPrice?.replace(Regex("\\s+"), "")?.toIntOrNull()

    fun isIncreaseAvailable() : Boolean = item.quantity > count

    fun isDecreaseAvailable() : Boolean = item.quantity < count
}

fun ShopItemCart.calculateTotalCost() = numberPrice * count

