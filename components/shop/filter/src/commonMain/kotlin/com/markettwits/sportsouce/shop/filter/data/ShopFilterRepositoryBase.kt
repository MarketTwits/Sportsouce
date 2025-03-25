package com.markettwits.sportsouce.shop.filter.data

import com.markettwits.sportsouce.shop.cloud.api.SportSauceShopApi
import com.markettwits.sportsouce.shop.filter.data.mapper.ShopFilterMapper
import com.markettwits.sportsouce.shop.filter.domain.ShopFilterRepository
import com.markettwits.sportsouce.shop.filter.domain.models.ShopCategoryItem
import com.markettwits.sportsouce.shop.filter.domain.models.ShopFilterPrice
import com.markettwits.sportsouce.shop.filter.domain.models.ShopOptionInfo

class ShopFilterRepositoryBase(
    private val service: SportSauceShopApi,
    private val shopFilterMapper: ShopFilterMapper,
) : ShopFilterRepository {

    override suspend fun filter(): Result<List<ShopCategoryItem>> = runCatching {
        shopFilterMapper.mapCategories(service.categories())
    }

    override suspend fun options(catalogId: Int): Result<Pair<List<ShopOptionInfo>, ShopFilterPrice>> =
        runCatching {
            shopFilterMapper.mapRenderFilter(service.renderFilter(catalogId))
        }
}