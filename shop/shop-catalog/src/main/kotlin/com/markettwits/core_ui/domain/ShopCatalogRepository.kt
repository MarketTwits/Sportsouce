package com.markettwits.core_ui.domain

import com.markettwits.cloud_shop.api.SportSauceShopApi

interface ShopCatalogRepository {
    suspend fun products(): SportSauceShopApi
}