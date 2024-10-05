package com.markettwits.shop.catalog.domain.models

import com.markettwits.shop.cart.domain.ShopItemCart
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
data class ShopItem(
    val uuid: String,
    val currentPrice: String,
    val previousPrice: String?,
    val discount: Int?,
    val quantity : Int,
    val title: String,
    val imageUrl: List<String>,
)

fun ShopItem.mapToCart(): ShopItemCart = ShopItemCart(
    id = Random.nextInt(),
    currentPrice = currentPrice,
    previousPrice = previousPrice ?: "",
    imageUrl = imageUrl,
    quantity = quantity,
    title = title,
    uuid = uuid,
)

fun ShopItemCart.mapToShopItem() : ShopItem = ShopItem(
    uuid = uuid,
    currentPrice = currentPrice,
    previousPrice = previousPrice,
    discount = null,
    title = title,
    quantity = quantity,
    imageUrl = imageUrl
)