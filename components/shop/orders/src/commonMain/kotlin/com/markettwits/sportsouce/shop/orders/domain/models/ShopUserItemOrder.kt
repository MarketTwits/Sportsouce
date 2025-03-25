package com.markettwits.sportsouce.shop.orders.domain.models

data class ShopUserItemOrder(
    val brand: String,
    val code: String,
    val description: String,
    val discountPrice: Int,
    val id: String,
    val images: List<String>,
    val model: String,
    val name: String,
    val orderProduct: CostWithCount,
    val price: String
){
    data class CostWithCount(
        val cost : String,
        val count : Int
    )
}