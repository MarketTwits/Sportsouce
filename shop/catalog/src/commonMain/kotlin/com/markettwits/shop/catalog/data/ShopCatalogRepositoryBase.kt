package com.markettwits.shop.catalog.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import app.cash.paging.PagingData
import com.markettwits.cloud_shop.api.SportSauceShopApi
import com.markettwits.shop.catalog.domain.ShopCatalogRepository
import com.markettwits.shop.catalog.domain.mapper.ShopProductsCategoriesMapper
import com.markettwits.shop.catalog.domain.mapper.ShopProductsMapper
import com.markettwits.shop.catalog.domain.models.ShopCategoryItem
import com.markettwits.shop.catalog.domain.models.ShopFilterPrice
import com.markettwits.shop.catalog.domain.models.ShopItem
import com.markettwits.shop.paging.SHOP_ITEMS_PAGE_SIZE
import com.markettwits.shop.paging.ShopCatalogPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ShopCatalogRepositoryBase(
    private val cloudService: SportSauceShopApi,
    private val productMapper: ShopProductsMapper,
    private val categoriesMapper: ShopProductsCategoriesMapper,
) : ShopCatalogRepository {

    override fun paddingProducts(
        categoryId: Int?,
        options: Set<String>,
        price: ShopFilterPrice,
    ): Flow<PagingData<ShopItem>> {
        return Pager(
            PagingConfig(pageSize = SHOP_ITEMS_PAGE_SIZE)
        ) {
            ShopCatalogPagingSource(cloudService, categoryId, options, price)
        }.flow.map { it.map { data -> productMapper.map(data) } }
    }

    override suspend fun categories(): Result<List<ShopCategoryItem>> = runCatching {
        categoriesMapper.map(cloudService.categories())
    }

    override fun pagingProducts(categoryId: Int?): Flow<PagingData<ShopItem>> {
        return Pager(
            PagingConfig(pageSize = SHOP_ITEMS_PAGE_SIZE)
        ) {
            ShopCatalogPagingSource(cloudService, categoryId)
        }.flow.map { it.map { data -> productMapper.map(data) } }
    }


}