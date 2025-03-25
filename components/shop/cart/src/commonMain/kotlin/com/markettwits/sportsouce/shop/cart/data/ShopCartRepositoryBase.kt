package com.markettwits.sportsouce.shop.cart.data

import com.markettwits.cahce.InStorageListCache
import com.markettwits.sportsouce.auth.service.api.AuthDataSource
import com.markettwits.sportsouce.shop.cart.domain.ShopCartRepository
import com.markettwits.sportsouce.shop.cart.domain.ShopItemCart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class ShopCartRepositoryBase(
    private val cacheDataSource : InStorageListCache<ShopItemCart>,
    private val authDataSource: AuthDataSource
) : ShopCartRepository {

    override suspend fun createOrderAvailable(): Result<Unit> = runCatching {
        authDataSource.sharedUser().getOrThrow()
    }

    override suspend fun add(shopItemCart: ShopItemCart) {
        cacheDataSource.update {
            it?.let {
                val currentItem = it.find { t1 -> t1.item.id == shopItemCart.item.id } ?: shopItemCart
                if (currentItem.count <= 0)
                    it.plus(currentItem.copy(count = currentItem.count + 1))
                else{
                    it.map { value ->
                        if (value.item.id == shopItemCart.item.id)
                            value.copy(count = value.count + 1)
                        else
                            value
                    }
                }
            }
        }
    }

    override suspend fun remove(shopItemCart: ShopItemCart) {
        cacheDataSource.update {
            it?.let {
                val currentItem = it.find { t1 -> t1.item.id == shopItemCart.item.id } ?: shopItemCart
                if (currentItem.count <= 1)
                    it.minus(currentItem)
                else{
                    it.map { value ->
                        if (value.item.id == shopItemCart.item.id)
                            value.copy(count = value.count - 1)
                        else
                            value
                    }
                }
            }
        }
    }

    override suspend fun delete(shopItemCart: ShopItemCart) {
        cacheDataSource.remove(value = shopItemCart)
    }

    override suspend fun fetchAll(): List<ShopItemCart> = cacheDataSource.getList()

    override suspend fun fetchByUUID(uuid : String): ShopItemCart =
         cacheDataSource.getList().first { it.item.id == uuid  }

    override fun observe(): Flow<List<ShopItemCart>> = cacheDataSource.observe().map { it ?: emptyList() }
}
