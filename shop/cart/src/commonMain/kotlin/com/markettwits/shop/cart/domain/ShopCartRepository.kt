package com.markettwits.shop.cart.domain

import kotlinx.coroutines.flow.Flow

interface ShopCartRepository{

    suspend fun add(shopItemCart: ShopItemCart)

    suspend fun remove(shopItemCart: ShopItemCart)

    suspend fun fetchAll() : List<ShopItemCart>

    suspend fun fetchByUUID(uuid : String) : ShopItemCart?

    fun observe() : Flow<List<ShopItemCart>>

}
