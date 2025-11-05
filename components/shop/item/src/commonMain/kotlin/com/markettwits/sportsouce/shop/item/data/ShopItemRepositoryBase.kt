package com.markettwits.sportsouce.shop.item.data

import app.cash.paging.*
import com.markettwits.core.paging.OffsetAndLimitPagingSourceNew
import com.markettwits.sportsouce.shop.cloud.api.SportSauceShopApi
import com.markettwits.sportsouce.shop.cloud.model.product.Product
import com.markettwits.sportsouce.shop.domain.mapper.ShopProductsMapper
import com.markettwits.sportsouce.shop.domain.model.ShopItem
import com.markettwits.sportsouce.shop.item.data.cache.ShopItemLocalCache
import com.markettwits.sportsouce.shop.item.data.mapper.ShopPageItemMapper
import com.markettwits.sportsouce.shop.item.domain.ShopItemRepository
import com.markettwits.sportsouce.shop.item.domain.models.ShopExtraOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class ShopItemRepositoryBase(
    private val cloudService: SportSauceShopApi,
    private val cache: ShopItemLocalCache,
    private val mapper: ShopPageItemMapper,
    private val productMapper: ShopProductsMapper,
) : ShopItemRepository {

    override suspend fun item(id: String): Result<Pair<ShopItem, List<ShopExtraOptions>>> =
        runCatching {
            val newItem = mapper.map(cloudService.product(id))
            val prevItem = cache.fetch(id) ?: newItem
            val sortedItem = mapper.mapWithPrevOptions(newItem, prevItem)
            cache.add(sortedItem.first, sortedItem.second)
            sortedItem
        }

    override fun similarProducts(categoryId: Int?): Flow<PagingData<ShopItem>> {
        val pager: Pager<Int, Product> = run {
            val pagingConfig = PagingConfig(
                pageSize = SHOP_ITEMS_PAGE_SIZE,
                initialLoadSize = SHOP_ITEMS_PAGE_SIZE,
                prefetchDistance = SHOP_ITEMS_PAGE_SIZE / 2,
                enablePlaceholders = false,
                maxSize = Int.MAX_VALUE,
                jumpThreshold = Int.MIN_VALUE
            )
            Pager(
                config = pagingConfig,
                initialKey = null,
                pagingSourceFactory = {
                    ShopItemPagingSource(
                        shopApi = cloudService,
                        categoryId = categoryId
                    ) as PagingSource<Int, Product>
                }
            )
        }
        return pager.flow.map { it.map { value -> productMapper.map(value) } }
    }

    companion object {
        const val SHOP_ITEMS_PAGE_SIZE = 20
    }
}

class ShopItemPagingSource(
    private val shopApi: SportSauceShopApi,
    private val categoryId: Int?,
) : OffsetAndLimitPagingSourceNew<Product>(ShopItemRepositoryBase.SHOP_ITEMS_PAGE_SIZE) {

    override suspend fun getTotalCount(): Int = 0

    override suspend fun load(offset: Int, limit: Int): List<Product> {
        return shopApi.products(
            limit = limit,
            offset = offset,
            categoryId = categoryId
        )
    }
}