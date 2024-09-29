package com.markettwits.shop.paging

import com.markettwits.cloud_shop.api.SportSauceShopApi
import com.markettwits.cloud_shop.model.products.ProductsRemoteRow
import com.markettwits.shop.catalog.domain.models.ShopFilterPrice


internal const val SHOP_ITEMS_PAGE_SIZE = 30

class ShopCatalogPagingSource(
    private val shopApi: SportSauceShopApi,
    private val categoryId: Int? = null,
    private val options: Set<String>? = null,
    private val price: ShopFilterPrice? = null,
) : OffsetAndLimitPagingSource<ProductsRemoteRow>(SHOP_ITEMS_PAGE_SIZE) {

    override suspend fun load(offset: Int, limit: Int): List<ProductsRemoteRow> {
        return shopApi.products(limit, offset, categoryId, options = options).rows
    }

}