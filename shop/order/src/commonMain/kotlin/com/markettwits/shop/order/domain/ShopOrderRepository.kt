package com.markettwits.shop.order.domain

interface ShopOrderRepository {

    suspend fun createOrder()

}