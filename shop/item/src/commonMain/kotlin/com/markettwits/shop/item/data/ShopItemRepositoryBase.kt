package com.markettwits.shop.item.data

import com.markettwits.cloud_shop.api.SportSauceShopApi
import com.markettwits.shop.item.domain.ShopItemRepository
import com.markettwits.shop.item.domain.mapper.ShopPageItemMapper
import com.markettwits.shop.item.domain.models.ShopPageItem


class ShopItemRepositoryBase(
    private val cloudService: SportSauceShopApi,
    private val mapper: ShopPageItemMapper,
) : ShopItemRepository {

    override suspend fun item(id: String): Result<ShopPageItem> = kotlin.runCatching {
        mapper.map(cloudService.product(id))
    }
}