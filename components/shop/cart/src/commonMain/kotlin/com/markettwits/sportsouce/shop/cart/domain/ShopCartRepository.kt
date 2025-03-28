package com.markettwits.sportsouce.shop.cart.domain

import kotlinx.coroutines.flow.Flow

interface ShopCartRepository{

    suspend fun createOrderAvailable() : Result<Unit>

    suspend fun add(shopItemCart: ShopItemCart)

    suspend fun remove(shopItemCart: ShopItemCart)

    suspend fun delete(shopItemCart: ShopItemCart)

    suspend fun fetchAll() : List<ShopItemCart>

    suspend fun fetchByUUID(uuid : String) : ShopItemCart?

    fun observe() : Flow<List<ShopItemCart>>

}
