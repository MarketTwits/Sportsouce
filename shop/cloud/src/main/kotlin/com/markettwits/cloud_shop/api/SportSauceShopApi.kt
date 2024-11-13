package com.markettwits.cloud_shop.api

import com.markettwits.cloud_shop.model.categories.ChildrenItem
import com.markettwits.cloud_shop.model.common.OptionInfo
import com.markettwits.cloud_shop.model.option.OptionRemote
import com.markettwits.cloud_shop.model.order.request.CreateShopOrderRequest
import com.markettwits.cloud_shop.model.order.response.CreateShopOrderResponse
import com.markettwits.cloud_shop.model.product.Product
import com.markettwits.cloud_shop.model.product.ProductRemote
import com.markettwits.cloud_shop.model.renderFilter.RenderFilterRemote


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