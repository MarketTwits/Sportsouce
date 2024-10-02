package com.markettwits.shop.catalog.domain

import app.cash.paging.PagingData
import com.markettwits.shop.catalog.domain.models.ShopItem
import com.markettwits.shop.filter.domain.models.ShopCategoryItem
import com.markettwits.shop.filter.domain.models.ShopFilterPrice
import kotlinx.coroutines.flow.Flow

interface ShopCatalogRepository {

    suspend fun categories(): Result<List<ShopCategoryItem>>

    fun pagingProducts(categoryId: Int?): Flow<PagingData<ShopItem>>

    fun paddingProducts(
        categoryId: Int?,
        options: Set<String>,
        price: ShopFilterPrice,
    ): Flow<PagingData<ShopItem>>

    fun paddingProducts(
        query : String
    ) : Flow<PagingData<ShopItem>>

}