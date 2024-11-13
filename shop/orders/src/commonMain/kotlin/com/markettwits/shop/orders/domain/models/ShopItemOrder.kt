package com.markettwits.shop.orders.domain.models

data class ShopItemOrder(
    val brand: String,
    val code: String,
    val description: String,
    val discountPrice: Int,
    val id: String,
    val images: List<String>,
    val model: String,
    val name: String,
    val orderProduct: CostWithCount,
    val price: Int
){
    data class CostWithCount(
        val cost : String,
        val count : Int
    )
}