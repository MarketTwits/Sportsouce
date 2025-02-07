package com.markettwits.shop.catalog.data

import com.markettwits.cloud_shop.api.SportSauceShopApi
import com.markettwits.shop.catalog.domain.ShopCatalogRepository
import com.markettwits.shop.domain.mapper.ShopProductsMapper

class ShopCatalogRepositoryBase(
    private val cloudService: SportSauceShopApi,
    private val productMapper: ShopProductsMapper,
) : ShopCatalogRepository {

//    override fun paddingProducts(
//        categoryId: Int?,
//        options: List<String>?,
//        maxPrice: Int?,
//        minPrice : Int?
//    ): Flow<PagingData<ShopItem>> {
//        return Pager<Int, ShopItem>(
//            PagingConfig(
//                pageSize = SHOP_ITEMS_PAGE_SIZE,
//                initialLoadSize = SHOP_ITEMS_PAGE_SIZE,
//            )
//        ) {
//            ShopCatalogPagingSource(
//                cloudService,
//                ShopCatalogParams.WithFilter(categoryId, options, maxPrice, minPrice)
//            )
//        }.flow.map {  it.map { data -> productMapper.map(data) } }
//    }
//
//    override fun paddingProducts(query: String): Flow<PagingData<ShopItem>> {
//        return Pager(
//            PagingConfig(pageSize = SHOP_ITEMS_PAGE_SIZE)
//        ) {
//            ShopCatalogPagingSource(cloudService, ShopCatalogParams.WithQuery(query))
//        }.flow.map {
//            it.map { data -> productMapper.map(data) }
//        }
//    }
}