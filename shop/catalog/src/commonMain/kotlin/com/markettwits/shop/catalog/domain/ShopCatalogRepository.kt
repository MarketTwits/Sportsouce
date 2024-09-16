package com.markettwits.shop.catalog.domain

import com.markettwits.shop.catalog.presentation.cards.ShopItem

interface ShopCatalogRepository {

    suspend fun products(): Result<List<ShopItem>>

}