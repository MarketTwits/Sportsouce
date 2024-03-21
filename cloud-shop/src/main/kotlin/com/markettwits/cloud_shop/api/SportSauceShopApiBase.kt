package com.markettwits.cloud_shop.api

import com.markettwits.cloud_shop.model.categories.ChildrenItem
import com.markettwits.cloud_shop.model.filter.FiltersRemote
import com.markettwits.cloud_shop.model.product.ProductRemote
import com.markettwits.cloud_shop.model.products.ProductsRemote
import com.markettwits.core_cloud.provider.HttpClientProvider
import io.ktor.client.call.body
import io.ktor.client.request.get

internal class SportSauceShopApiBase(
    private val httpClient: HttpClientProvider
) : SportSauceShopApi {

    private val json = httpClient.json()
    private val client = httpClient.provide(true)

    override suspend fun categories(): List<ChildrenItem> {
        val response = client.get("category")
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun filters(): FiltersRemote {
        val response = client.get("product/render-filter")
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun products(): ProductsRemote {
        val response = client.get("product")
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun product(uuid: String): ProductRemote {
        val response = client.get("product/$uuid")
        return json.decodeFromString(response.body<String>())
    }

}