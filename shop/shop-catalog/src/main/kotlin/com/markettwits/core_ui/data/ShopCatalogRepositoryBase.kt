package com.markettwits.core_ui.data

import com.markettwits.cloud_shop.api.SportSauceShopApi
import com.markettwits.core_ui.domain.ShopCatalogRepository

class ShopCatalogRepositoryBase : ShopCatalogRepository {
    override suspend fun products(): SportSauceShopApi {
        TODO("Not yet implemented")
    }
}