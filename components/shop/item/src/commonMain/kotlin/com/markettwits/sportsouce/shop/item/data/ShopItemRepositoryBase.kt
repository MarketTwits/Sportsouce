package com.markettwits.sportsouce.shop.item.data

import com.markettwits.sportsouce.shop.cloud.api.SportSauceShopApi
import com.markettwits.sportsouce.shop.domain.model.ShopItem
import com.markettwits.sportsouce.shop.item.data.cache.ShopItemLocalCache
import com.markettwits.sportsouce.shop.item.data.mapper.ShopPageItemMapper
import com.markettwits.sportsouce.shop.item.domain.ShopItemRepository
import com.markettwits.sportsouce.shop.item.domain.models.ShopExtraOptions


class ShopItemRepositoryBase(
    private val cloudService: SportSauceShopApi,
    private val cache: ShopItemLocalCache,
    private val mapper: ShopPageItemMapper,
) : ShopItemRepository {

    override suspend fun item(id: String): Result<Pair<ShopItem, List<ShopExtraOptions>>> =
        runCatching {
            val newItem = mapper.map(cloudService.product(id))
            val prevItem = cache.fetch(id) ?: newItem
            val sortedItem = mapper.mapWithPrevOptions(newItem, prevItem)
            cache.add(sortedItem.first, sortedItem.second)
            sortedItem
        }
}