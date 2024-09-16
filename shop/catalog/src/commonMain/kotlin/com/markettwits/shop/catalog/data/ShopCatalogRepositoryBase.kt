package com.markettwits.shop.catalog.data

import com.markettwits.cloud_shop.api.SportSauceShopApi
import com.markettwits.shop.catalog.domain.ShopCatalogRepository
import com.markettwits.shop.catalog.domain.mapper.ShopProductsMapper
import com.markettwits.shop.catalog.presentation.cards.ShopItem

class ShopCatalogRepositoryBase(
    private val cloudService: SportSauceShopApi,
    private val mapper: ShopProductsMapper,
) : ShopCatalogRepository {

    override suspend fun products(): Result<List<ShopItem>> = runCatching {
        if (ShopCatalogLocalCache.fetch().isNotEmpty()) {
            return Result.success(ShopCatalogLocalCache.fetch())
        } else {
            val items = mapper.map(cloudService.products().rows)
            ShopCatalogLocalCache.write(items)
            items
        }

    }
}

data object ShopCatalogLocalCache {

    private val innerItems = emptyList<ShopItem>().toMutableList()

    fun write(items: List<ShopItem>) {
        innerItems.addAll(items)
    }

    fun fetch(): List<ShopItem> = innerItems
}