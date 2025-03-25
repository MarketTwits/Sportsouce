package com.markettwits.sportsouce.shop.catalog.domain

import com.markettwits.sportsouce.shop.cloud.api.SportSauceShopApi
import com.markettwits.sportsouce.shop.cloud.model.product.Product
import com.markettwits.core.paging.OffsetAndLimitPagingSourceNew

internal const val SHOP_ITEMS_PAGE_SIZE = 20

class ShopCatalogPagingSource(
    private val shopApi: SportSauceShopApi,
    private val params: ShopCatalogParams,
    ) : OffsetAndLimitPagingSourceNew<Product>(SHOP_ITEMS_PAGE_SIZE) {

    override suspend fun load(offset: Int, limit: Int): List<Product> {
        return when (params) {
            is ShopCatalogParams.WithFilter -> shopApi.products(
                limit = limit,
                offset = offset,
                categoryId = params.categoryId,
                options = params.options,
                priceMax = params.maxPrice,
                priceMin = params.minPrice
            )

            is ShopCatalogParams.WithQuery -> shopApi.products(
                limit = limit,
                offset = offset,
                query = params.query
            )
        }
    }
}


