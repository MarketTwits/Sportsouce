package com.markettwits.shop.orders.domain

import com.markettwits.shop.orders.domain.models.ShopUserOrder
import kotlinx.coroutines.flow.Flow

interface ShopUserOrdersRepository {

    suspend fun getUserOrders(forced : Boolean = false) : Flow<List<ShopUserOrder>>

}