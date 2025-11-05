package com.markettwits.sportsouce.shop.item.domain

import app.cash.paging.PagingData
import com.markettwits.sportsouce.shop.domain.model.ShopItem
import com.markettwits.sportsouce.shop.item.domain.models.ShopExtraOptions
import kotlinx.coroutines.flow.Flow

interface ShopItemRepository {

    suspend fun item(id: String): Result<Pair<ShopItem,List<ShopExtraOptions>>>

    fun similarProducts(categoryId: Int?): Flow<PagingData<ShopItem>>

}