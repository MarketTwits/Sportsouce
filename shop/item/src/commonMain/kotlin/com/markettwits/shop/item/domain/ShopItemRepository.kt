package com.markettwits.shop.item.domain

import com.markettwits.shop.item.domain.models.ShopPageItem

interface ShopItemRepository {

    suspend fun item(id: String): Result<ShopPageItem>

}