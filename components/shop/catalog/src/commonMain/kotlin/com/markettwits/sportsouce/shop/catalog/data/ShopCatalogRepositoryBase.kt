package com.markettwits.sportsouce.shop.catalog.data


import app.cash.paging.*
import com.markettwits.sportsouce.shop.cloud.api.SportSauceShopApi
import com.markettwits.sportsouce.shop.cloud.model.product.Product
import com.markettwits.sportsouce.shop.catalog.domain.ShopCatalogRepository
import com.markettwits.sportsouce.shop.domain.mapper.ShopProductsMapper
import com.markettwits.sportsouce.shop.domain.model.ShopItem
import com.markettwits.sportsouce.shop.catalog.domain.SHOP_ITEMS_PAGE_SIZE
import com.markettwits.sportsouce.shop.catalog.domain.ShopCatalogPagingSource
import com.markettwits.sportsouce.shop.catalog.domain.ShopCatalogParams
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
        minPrice: Int?
    ): Flow<PagingData<ShopItem>> {
        val pager: Pager<Int, Product> = run {
            val pagingConfig = PagingConfig(pageSize = SHOP_ITEMS_PAGE_SIZE, initialLoadSize = SHOP_ITEMS_PAGE_SIZE)
            Pager(pagingConfig) {
                ShopCatalogPagingSource(
                    shopApi = cloudService,
                    params = ShopCatalogParams.WithFilter(categoryId, options, maxPrice, minPrice)
                ) as PagingSource<Int, Product>
            }
        }
        return pager.flow.map { it.map { value -> productMapper.map(value) } }
    }

    override fun paddingProducts(query: String): Flow<PagingData<ShopItem>> {
        val pager: Pager<Int, Product> = run {
            val pagingConfig = PagingConfig(pageSize = SHOP_ITEMS_PAGE_SIZE,initialLoadSize = SHOP_ITEMS_PAGE_SIZE)
            Pager(pagingConfig) {
                ShopCatalogPagingSource(
                    shopApi = cloudService,
                    params = ShopCatalogParams.WithQuery(query)
                ) as PagingSource<Int, Product>
            }
        }
        return pager.flow.map { it.map { value -> productMapper.map(value) } }
    }
}