package com.markettwits.shop.catalog.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import app.cash.paging.PagingData
import com.markettwits.cloud_shop.api.SportSauceShopApi
import com.markettwits.shop.catalog.domain.ShopCatalogRepository
import com.markettwits.shop.domain.mapper.ShopProductsMapper
import com.markettwits.shop.domain.model.ShopItem
import com.markettwits.shop.filter.data.mapper.ShopProductsCategoriesMapper
import com.markettwits.shop.filter.domain.models.ShopCategoryItem
import com.markettwits.shop.filter.domain.models.ShopFilterPrice
import com.markettwits.shop.paging.SHOP_ITEMS_PAGE_SIZE
import com.markettwits.shop.paging.ShopCatalogPagingSource
import com.markettwits.shop.paging.ShopCatalogParams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ShopCatalogRepositoryBase(
    private val cloudService: SportSauceShopApi,
    private val productMapper: ShopProductsMapper,
) : ShopCatalogRepository {

    override fun paddingProducts(
        categoryId: Int?,
        options: List<String>?,
        maxPrice: Int?,
        minPrice : Int?
    ): Flow<PagingData<ShopItem>> {
        return Pager(
            PagingConfig(pageSize = SHOP_ITEMS_PAGE_SIZE)
        ) {
            ShopCatalogPagingSource(
                cloudService,
                ShopCatalogParams.WithFilter(categoryId, options, maxPrice, minPrice)
            )
        }.flow.map {  it.map { data -> productMapper.map(data) } }
    }

    override fun paddingProducts(query: String): Flow<PagingData<ShopItem>> {
        return Pager(
            PagingConfig(pageSize = SHOP_ITEMS_PAGE_SIZE)
        ) {
            ShopCatalogPagingSource(cloudService, ShopCatalogParams.WithQuery(query))
        }.flow.map {
            it.map { data -> productMapper.map(data) }
        }
    }
}