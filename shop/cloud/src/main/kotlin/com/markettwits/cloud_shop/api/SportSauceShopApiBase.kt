package com.markettwits.cloud_shop.api

import com.markettwits.cloud_shop.model.categories.ChildrenItem
import com.markettwits.cloud_shop.model.option.OptionRemote
import com.markettwits.cloud_shop.model.product.ProductRemote
import com.markettwits.cloud_shop.model.products.ProductsRemote
import com.markettwits.cloud_shop.model.renderFilter.RenderFilterRemote
import com.markettwits.core_cloud.provider.HttpClientProvider
import io.ktor.client.call.body
import io.ktor.client.request.get

internal class SportSauceShopApiBase(
    private val httpClient: HttpClientProvider,
    private val isLoggerEnabled: Boolean = false,
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

    override suspend fun products(): ProductsRemote {
        val response = client.get("product")
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun products(
        limit: Int?,
        offset: Int?,
        categoryId: Int?,
        options: Set<String>?,
        priceMax : Int?,
        priceMin : Int?
    ): ProductsRemote {
        val response = client.get("product") {
            url {
                if (categoryId != null) {
                    parameters.append("category_id", categoryId.toString())
                }
                if (limit != null) {
                    parameters.append("limit", limit.toString())
                }
                if (offset != null) {
                    parameters.append("offset", offset.toString())
                }
                if (options != null) {
                    parameters.append("options%5B%5D", options.joinToString(","))
                }
                if (priceMax != null){
                    parameters.append("max_price", priceMax.toString())
                }
                if (priceMin != null){
                    parameters.append("mnx_price", priceMin.toString())
                }
            }
        }
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun products(limit: Int, offset: Int, query: String): ProductsRemote {
        val response = client.get("product/deep-search") {
            url {
                parameters.append("limit", limit.toString())
                parameters.append("offset", offset.toString())
                parameters.append("search", query.trim())
            }
        }
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun products(
        limit: Int,
        offset: Int,
    ): ProductsRemote {
        val response = client.get("product") {
            url {
                parameters.append("limit", limit.toString())
                parameters.append("offset", offset.toString())
            }
        }
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun product(uuid: String): ProductRemote {
        val response = client.get("product/$uuid")
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun productOption(): OptionRemote {
        val response = client.get("product-option") {
            url {
                parameters.append("maxResultCount", "1000")
                parameters.append("skipCount", "0")
            }
        }
        return json.decodeFromString(response.body<String>())
    }
}