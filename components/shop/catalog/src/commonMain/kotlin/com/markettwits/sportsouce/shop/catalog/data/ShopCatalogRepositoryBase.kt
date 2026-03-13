package com.markettwits.sportsouce.shop.catalog.data


import app.cash.paging.*
import com.markettwits.sportsouce.shop.catalog.domain.SHOP_ITEMS_PAGE_SIZE
import com.markettwits.sportsouce.shop.catalog.domain.ShopCatalogPagingSource
import com.markettwits.sportsouce.shop.catalog.domain.ShopCatalogParams
import com.markettwits.sportsouce.shop.catalog.domain.ShopCatalogRepository
import com.markettwits.sportsouce.shop.cloud.api.SportSauceShopApi
import com.markettwits.sportsouce.shop.cloud.model.product.Product
import com.markettwits.sportsouce.shop.domain.mapper.ShopProductsMapper
import com.markettwits.sportsouce.shop.domain.model.ShopItem
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

    override suspend fun salesProducts(): Result<List<ShopItem>> = runCatching {
        categoryProducts(SALES_CATEGORY_ID_PROD)
    }

    override suspend fun merchProducts(): Result<List<ShopItem>> = runCatching {
        categoryProducts(MERCH_CATEGORY_ID)
    }

    private suspend fun categoryProducts(categoryId: Int): List<ShopItem> {
        val items = cloudService.products(
            limit = 20,
            offset = 0,
            categoryId = categoryId
        )
        return productMapper.map(items)
    }

    companion object {
        const val SALES_CATEGORY_ID_PROD = 577
        const val MERCH_CATEGORY_ID = 582
    }
}
