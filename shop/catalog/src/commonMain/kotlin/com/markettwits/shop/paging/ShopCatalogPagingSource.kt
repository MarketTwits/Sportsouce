package com.markettwits.shop.paging

import com.markettwits.cloud_shop.api.SportSauceShopApi
import com.markettwits.cloud_shop.model.product.Product
import com.markettwits.cloud_shop.model.product.ProductRemote
import com.markettwits.shop.filter.domain.models.ShopFilterPrice


internal const val SHOP_ITEMS_PAGE_SIZE = 30


class ShopCatalogPagingSource(
    private val shopApi: SportSauceShopApi,
    private val params: ShopCatalogParams,
    ) : OffsetAndLimitPagingSource<Product>(SHOP_ITEMS_PAGE_SIZE) {

    override suspend fun load(offset: Int, limit: Int): List<Product> {
        return when (params) {
            is ShopCatalogParams.WithFilter -> shopApi.products(
                limit = limit,
                offset = offset,
                categoryId = params.categoryId,
                options = params.options,
                priceMax = params.price?.maxPrice?.apply(),
                priceMin = params.price?.minPrice?.apply()
            ).rows

            is ShopCatalogParams.WithQuery -> shopApi.products(
                limit = limit,
                offset = offset,
                query = params.query
            ).rows
        }
    }
}


sealed interface ShopCatalogParams {

    data class WithFilter(
        val categoryId: Int? = null,
        val options: List<String>? = null,
        val price: ShopFilterPrice? = null,
    ) : ShopCatalogParams

    data class WithQuery(
        val query: String
    ) : ShopCatalogParams

}