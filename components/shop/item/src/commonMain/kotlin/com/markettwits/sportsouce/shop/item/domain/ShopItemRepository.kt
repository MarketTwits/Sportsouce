package com.markettwits.sportsouce.shop.item.domain

import com.markettwits.sportsouce.shop.domain.model.ShopItem
import com.markettwits.sportsouce.shop.item.domain.models.ShopExtraOptions

interface ShopItemRepository {

    suspend fun item(id: String): Result<Pair<ShopItem,List<ShopExtraOptions>>>

}