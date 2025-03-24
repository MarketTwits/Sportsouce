package com.markettwits.shop.item.domain

import com.markettwits.shop.domain.model.ShopItem
import com.markettwits.shop.item.domain.models.ShopExtraOptions

interface ShopItemRepository {

    suspend fun item(id: String): Result<Pair<ShopItem,List<ShopExtraOptions>>>

}