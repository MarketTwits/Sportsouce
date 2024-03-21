package com.markettwits.shop_catalog.domain

interface ShopCatalogRepository {
    suspend fun products(): SportSauceShopApi
}