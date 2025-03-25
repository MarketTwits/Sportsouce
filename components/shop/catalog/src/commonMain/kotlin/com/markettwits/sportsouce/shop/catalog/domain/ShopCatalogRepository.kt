package com.markettwits.sportsouce.shop.catalog.domain

import app.cash.paging.PagingData
import com.markettwits.sportsouce.shop.domain.model.ShopItem
import kotlinx.coroutines.flow.Flow

interface ShopCatalogRepository {

    fun paddingProducts(
        categoryId: Int?,
        options: List<String>?,
        maxPrice: Int?,
        minPrice : Int?
    ): Flow<PagingData<ShopItem>>

    fun paddingProducts(
        query : String
    ) : Flow<PagingData<ShopItem>>

}