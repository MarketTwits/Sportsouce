package com.markettwits.shop.catalog.domain

internal const val SHOP_ITEMS_PAGE_SIZE = 30


//class ShopCatalogPagingSource(
//    private val shopApi: SportSauceShopApi,
//    private val params: ShopCatalogParams,
//    ) : OffsetAndLimitPagingSource<Product>(SHOP_ITEMS_PAGE_SIZE) {
//
//    override suspend fun load(offset: Int, limit: Int): List<Product> {
//        return when (params) {
//            is ShopCatalogParams.WithFilter -> shopApi.products(
//                limit = limit,
//                offset = offset,
//                categoryId = params.categoryId,
//                options = params.options,
//                priceMax = params.maxPrice,
//                priceMin = params.minPrice
//            )
//
//            is ShopCatalogParams.WithQuery -> shopApi.products(
//                limit = limit,
//                offset = offset,
//                query = params.query
//            )
//        }
//    }
//}


