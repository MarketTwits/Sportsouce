package com.markettwits.shop.filter.domain

import com.markettwits.shop.catalog.domain.models.ShopCategoryItem
import com.markettwits.shop.catalog.domain.models.ShopFilterPrice
import com.markettwits.shop.catalog.domain.models.ShopOptionInfo

interface ShopFilterRepository {

    suspend fun filter(): Result<List<ShopCategoryItem>>

    suspend fun options(catalogId: Int): Result<Pair<List<ShopOptionInfo>, ShopFilterPrice>>
}