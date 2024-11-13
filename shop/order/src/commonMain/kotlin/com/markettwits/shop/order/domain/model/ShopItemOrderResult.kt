package com.markettwits.shop.order.domain.model

import com.markettwits.shop.cart.domain.ShopItemCart
import kotlinx.serialization.Serializable

@Serializable
data class ShopItemOrderResult(
    val item : ShopItemCart,
    val isAvailable : Boolean,
    val message : String?
)

fun List<ShopItemOrderResult>.getAvailableItems() : List<ShopItemCart> =
    filter { it.isAvailable }
    .map { it.item }

fun List<ShopItemOrderResult>.containsUnAvailableItems() : Boolean =
    any { !it.isAvailable }

fun List<ShopItemOrderResult>.isAvailableToCreateOrder() : Boolean =
    any { it.item.numberPrice > 0 }