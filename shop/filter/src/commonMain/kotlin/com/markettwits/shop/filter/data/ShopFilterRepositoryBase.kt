package com.markettwits.shop.filter.data

import com.markettwits.cloud_shop.api.SportSauceShopApi
import com.markettwits.shop.filter.domain.ShopFilterRepository
import com.markettwits.shop.filter.domain.mapper.ShopFilterMapper
import com.markettwits.shop.filter.domain.models.ShopCategoryItem
import com.markettwits.shop.filter.domain.models.ShopFilterPrice
import com.markettwits.shop.filter.domain.models.ShopOptionInfo

class ShopFilterRepositoryBase(
    private val service: SportSauceShopApi,
    private val shopFilterMapper: ShopFilterMapper,
) : ShopFilterRepository {

    override suspend fun filter(): Result<List<ShopCategoryItem>> =
        runCatching {
            shopFilterMapper.mapCategories(service.categories())
        }

    override suspend fun options(catalogId: Int): Result<Pair<List<ShopOptionInfo>, ShopFilterPrice>> =
        runCatching {
            shopFilterMapper.mapRenderFilter(service.renderFilter(catalogId))
        }
}