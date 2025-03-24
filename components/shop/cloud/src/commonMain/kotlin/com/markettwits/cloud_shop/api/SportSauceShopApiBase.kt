package com.markettwits.cloud_shop.api

import com.markettwits.cloud_shop.model.categories.ChildrenItem
import com.markettwits.cloud_shop.model.common.OptionInfo
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
        val response = client.get("${Endpoints.PRODUCT}/${Endpoints.RENDER_FILTER}") {
            url {
                parameters.append(Query.CATEGORY_ID, categoryId.toString())
            }
        }
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun categories(): List<ChildrenItem> {
        val response = client.get(Endpoints.CATEGORIES)
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
        val response = client.get(Endpoints.PRODUCT) {
            url {
                listOfNotNull(
                    categoryId?.let { Query.CATEGORY_ID to it.toString() },
                    limit?.let { Query.LIMIT to it.toString() },
                    offset?.let { Query.OFFSET to it.toString() },
                    priceMax?.let { Query.MAX_PRICE to it.toString() },
                    priceMin?.let { Query.MIN_PRICE to it.toString() }
                ).forEach { (key, value) ->
                    parameters.append(key, value)
                }
                options?.forEach {
                    parameters.append(Query.OPTIONS, it)
                }
            }
        }
        return json.decodeFromString<ProductsRemote>(response.body<String>()).rows
    }

    override suspend fun products(limit: Int, offset: Int, query: String): List<Product> {
        val response = client.get("${Endpoints.PRODUCT}/${Endpoints.DEEP_SEARCH}") {
            url {
                parameters.append(Query.LIMIT, limit.toString())
                parameters.append(Query.OFFSET, offset.toString())
                parameters.append(Query.SEARCH, query.trim())
            }
        }
        return json.decodeFromString<ProductsRemote>(response.body<String>()).rows
    }

    override suspend fun product(uuid: String): ProductRemote {
        val response = client.get("${Endpoints.PRODUCT}/$uuid")
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun productOption(): List<OptionInfo> {
        val response = client.get(Endpoints.PRODUCT_OPTION) {
            url {
                parameters.append(Query.MAX_RESULT_COUNT, "1000")
            }
        }
        return json.decodeFromString<OptionRemote>(response.body<String>()).rows
    }

    private companion object{

        data object Endpoints{
            const val PRODUCT_OPTION = "product-option"
            const val PRODUCT = "product"
            const val CATEGORIES = "category"
            const val RENDER_FILTER = "render-filter"
            const val DEEP_SEARCH = "deep-search"
        }

        data object Query{
            const val LIMIT = "limit"
            const val OFFSET = "offset"
            const val CATEGORY_ID = "category_id"
            const val SEARCH = "search"
            const val MAX_PRICE = "max_price"
            const val MIN_PRICE = "min_price"
            const val OPTIONS = "options[]"
            const val MAX_RESULT_COUNT = "maxResultCount"
        }
    }

}