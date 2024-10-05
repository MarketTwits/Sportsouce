package com.markettwits.shop.cart.data

import com.markettwits.cahce.InStorageListCache
import com.markettwits.shop.cart.domain.ShopCartRepository
import com.markettwits.shop.cart.domain.ShopItemCart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class ShopCartRepositoryBase(
    private val datasource : InStorageListCache<ShopItemCart>
) : ShopCartRepository {

    override suspend fun add(shopItemCart: ShopItemCart) {
        datasource.update {
            it?.let {
                val currentItem = it.find { t1 -> t1.uuid == shopItemCart.uuid } ?: shopItemCart
                if (currentItem.count <= 0)
                    it.plus(currentItem.copy(count = currentItem.count + 1))
                else{
                    it.map { value ->
                        if (value.uuid == shopItemCart.uuid)
                            value.copy(count = value.count + 1)
                        else
                            value
                    }
                }
            }
        }
    }

    override suspend fun remove(shopItemCart: ShopItemCart) {
        datasource.update {
            it?.let {
                val currentItem = it.find { t1 -> t1.uuid == shopItemCart.uuid } ?: shopItemCart
                if (currentItem.count <= 1)
                    it.minus(currentItem)
                else{
                    it.map { value ->
                        if (value.uuid == shopItemCart.uuid)
                            value.copy(count = value.count - 1)
                        else
                            value
                    }
                }
            }
        }
    }

    override suspend fun fetchAll(): List<ShopItemCart> = datasource.getList()

    override suspend fun fetchByUUID(uuid : String): ShopItemCart =
         datasource.getList().first { it.uuid == uuid  }

    override fun observe(): Flow<List<ShopItemCart>> = datasource.observe().map { it ?: emptyList() }
}