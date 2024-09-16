package com.markettwits.cloud_shop.api

import com.markettwits.cloud_shop.model.categories.ChildrenItem
import com.markettwits.cloud_shop.model.product.ProductRemote
import com.markettwits.cloud_shop.model.products.ProductsRemote


interface SportSauceShopApi {

    suspend fun categories(): List<ChildrenItem>

    suspend fun products(): ProductsRemote

    suspend fun product(uuid: String): ProductRemote
}