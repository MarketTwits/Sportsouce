package com.markettwits.cloud_shop.api

import com.markettwits.cloud_shop.model.categories.ChildrenItem
import com.markettwits.cloud_shop.model.option.OptionRemote
import com.markettwits.cloud_shop.model.product.ProductRemote
import com.markettwits.cloud_shop.model.products.ProductsRemote
import com.markettwits.cloud_shop.model.renderFilter.RenderFilterRemote


interface SportSauceShopApi {

    suspend fun renderFilter(categoryId: Int): RenderFilterRemote

    suspend fun categories(): List<ChildrenItem>

    suspend fun products(): ProductsRemote

    suspend fun products(limit: Int, offset: Int): ProductsRemote

    suspend fun products(
        limit: Int? = null,
        offset: Int? = null,
        categoryId: Int? = null,
        options: Set<String>? = null,
    ): ProductsRemote

    suspend fun products(
        limit: Int,
        offset: Int,
        query : String
    ) : ProductsRemote

    suspend fun product(uuid: String): ProductRemote

    suspend fun productOption(): OptionRemote
}