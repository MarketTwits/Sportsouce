package com.markettwits.cloud_shop.api

import com.markettwits.cloud_shop.model.categories.ChildrenItem
import com.markettwits.cloud_shop.model.option.OptionRemote
import com.markettwits.cloud_shop.model.product.Product
import com.markettwits.cloud_shop.model.product.ProductRemote
import com.markettwits.cloud_shop.model.products.ProductsRemote
import com.markettwits.cloud_shop.model.renderFilter.RenderFilterRemote
import com.markettwits.core_cloud.provider.HttpClientProvider
import io.ktor.client.call.body
import io.ktor.client.request.get

internal class SportSauceShopApiBase(
    httpClient: HttpClientProvider,
    isLoggerEnabled: Boolean = false,
) : SportSauceShopApi {

    private val json = httpClient.json()

    private val client = httpClient.provide(isLoggerEnabled)

    override suspend fun renderFilter(categoryId: Int): RenderFilterRemote {
        val response = client.get("product/render-filter") {
            url {
                parameters.append("categoryId", categoryId.toString())
            }
        }
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun categories(): List<ChildrenItem> {
        val response = client.get("category")
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun products(
        limit: Int?,
        offset: Int?,
        categoryId: Int?,
        options: List<String>?,
        priceMax: Int?,
        priceMin: Int?
    ): List<Product> {
        val response = client.get("product") {
            url {
                listOfNotNull(
                    categoryId?.let { "category_id" to it.toString() },
                    limit?.let { "limit" to it.toString() },
                    offset?.let { "offset" to it.toString() },
                    priceMax?.let { "max_price" to it.toString() },
                    priceMin?.let { "min_price" to it.toString() }
                ).forEach { (key, value) ->
                    parameters.append(key, value)
                }
                options?.forEach {
                    parameters.append("options[]", it)
                }
            }
        }
        return json.decodeFromString<ProductsRemote>(response.body<String>()).rows
    }

    override suspend fun products(limit: Int, offset: Int, query: String): List<Product> {
        val response = client.get("product/deep-search") {
            url {
                parameters.append("limit", limit.toString())
                parameters.append("offset", offset.toString())
                parameters.append("search", query.trim())
            }
        }
        return json.decodeFromString<ProductsRemote>(response.body<String>()).rows
    }

    override suspend fun product(uuid: String): ProductRemote {
        val response = client.get("product/$uuid")
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun productOption(): OptionRemote {
        val response = client.get("product-option") {
            url {
                parameters.append("maxResultCount", "1000")
            }
        }
        return json.decodeFromString(response.body<String>())
    }
}