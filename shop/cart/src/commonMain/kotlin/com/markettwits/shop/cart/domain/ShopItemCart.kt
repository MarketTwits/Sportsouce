package com.markettwits.shop.cart.domain

import kotlinx.serialization.Serializable
import java.text.NumberFormat
import java.util.Locale

@Serializable
data class ShopItemCart(
    val id : Int,
    val uuid: String,
    val currentPrice: String,
    val quantity : Int,
    val previousPrice : String?,
    val title: String,
    val imageUrl: List<String>,
    val count : Int = 0
){
    val numberPrice = currentPrice.replace(Regex("\\s+"), "").toIntOrNull() ?: 0
    val numberPreviousPrice : Int? = previousPrice?.replace(Regex("\\s+"), "")?.toIntOrNull()
}
fun Int.formatPrice(): String {
    val formatter = NumberFormat.getInstance(Locale("ru", "RU"))
    return formatter.format(this)
}
