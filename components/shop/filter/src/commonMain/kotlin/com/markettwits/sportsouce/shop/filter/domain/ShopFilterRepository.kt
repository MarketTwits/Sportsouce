package com.markettwits.sportsouce.shop.filter.domain

import com.markettwits.sportsouce.shop.filter.domain.models.ShopCategoryItem
import com.markettwits.sportsouce.shop.filter.domain.models.ShopFilterPrice
import com.markettwits.sportsouce.shop.filter.domain.models.ShopOptionInfo

interface ShopFilterRepository {

    suspend fun filter(): Result<List<ShopCategoryItem>>

    suspend fun options(catalogId: Int): Result<Pair<List<ShopOptionInfo>, ShopFilterPrice>>
}