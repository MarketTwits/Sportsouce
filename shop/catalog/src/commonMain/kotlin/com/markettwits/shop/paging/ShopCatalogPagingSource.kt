package com.markettwits.shop.paging

import com.markettwits.cloud_shop.api.SportSauceShopApi
import com.markettwits.cloud_shop.model.products.ProductsRemoteRow
import com.markettwits.shop.filter.domain.models.ShopFilterPrice


internal const val SHOP_ITEMS_PAGE_SIZE = 30


class ShopCatalogPagingSource(
    private val shopApi: SportSauceShopApi,
    private val params: ShopCatalogParams,

    ) : OffsetAndLimitPagingSource<ProductsRemoteRow>(SHOP_ITEMS_PAGE_SIZE) {

    override suspend fun load(offset: Int, limit: Int): List<ProductsRemoteRow> {
        return when (params) {
            is ShopCatalogParams.WithFilter -> shopApi.products(
                limit,
                offset,
                params.categoryId,
                params.options
            ).rows
            is ShopCatalogParams.WithQuery -> shopApi.products(limit, offset, params.query).rows
        }
    }
}


sealed interface ShopCatalogParams {

    data class WithFilter(
        val categoryId: Int? = null,
        val options: Set<String>? = null,
        val price: ShopFilterPrice? = null,
    ) : ShopCatalogParams

    data class WithQuery(
        val query: String
    ) : ShopCatalogParams

}