package com.markettwits.sportsouce.shop.orders.domain

import com.markettwits.sportsouce.shop.orders.domain.models.ShopUserOrder
import kotlinx.coroutines.flow.Flow

interface ShopUserOrdersRepository {

    suspend fun getUserOrders(forced : Boolean = false) : Flow<List<ShopUserOrder>>

}