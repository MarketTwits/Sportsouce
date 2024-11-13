package com.markettwits.shop.item.data

import com.markettwits.cloud_shop.api.SportSauceShopApi
import com.markettwits.shop.domain.model.ShopItem
import com.markettwits.shop.item.data.cache.ShopItemLocalCache
import com.markettwits.shop.item.domain.ShopItemRepository
import com.markettwits.shop.item.data.mapper.ShopPageItemMapper
import com.markettwits.shop.item.domain.models.ShopExtraOptions


class ShopItemRepositoryBase(
    private val cloudService: SportSauceShopApi,
    private val cache: ShopItemLocalCache,
    private val mapper: ShopPageItemMapper,
) : ShopItemRepository {

    override suspend fun item(id: String): Result<Pair<ShopItem, List<ShopExtraOptions>>> = runCatching {
            val cacheValue = cache.fetch(id)
            if (cacheValue != null) {
                cacheValue
            } else {
                val cloud = mapper.map(cloudService.product(id))
                cache.add(cloud.first, cloud.second)
                cloud
            }
        }
}