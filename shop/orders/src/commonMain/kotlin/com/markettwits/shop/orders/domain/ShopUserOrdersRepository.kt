package com.markettwits.shop.orders.domain

import com.markettwits.shop.orders.domain.models.ShopUserOrder

interface ShopUserOrdersRepository {

    suspend fun getUserOrders() : Result<List<ShopUserOrder>>

}