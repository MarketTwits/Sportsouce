package com.markettwits.sportsouce.shop.cloud.api

import com.markettwits.sportsouce.shop.cloud.model.categories.ChildrenItem
import com.markettwits.sportsouce.shop.cloud.model.common.OptionInfo
import com.markettwits.sportsouce.shop.cloud.model.product.Product
import com.markettwits.sportsouce.shop.cloud.model.product.ProductRemote
import com.markettwits.sportsouce.shop.cloud.model.renderFilter.RenderFilterRemote


interface SportSauceShopApi {

    suspend fun renderFilter(categoryId: Int): RenderFilterRemote

    suspend fun categories(): List<ChildrenItem>

    suspend fun products(
        limit: Int? = null,
        offset: Int? = null,
        categoryId: Int? = null,
        options: List<String>? = null,
        priceMax : Int? = null,
        priceMin : Int? = null
    ): List<Product>

    suspend fun products(
        limit: Int,
        offset: Int,
        query : String
    ) : List<Product>

    suspend fun product(uuid: String): ProductRemote

    suspend fun productOption(): List<OptionInfo>
}