package com.markettwits.cloud_shop.api

import com.markettwits.cloud_shop.model.check.request.CheckShopOrderRequest
import com.markettwits.cloud_shop.model.check.response.CheckShopOrderResponseItem
import com.markettwits.cloud_shop.model.order.common.Order
import com.markettwits.cloud_shop.model.order.request.CreateShopOrderRequest
import com.markettwits.cloud_shop.model.order.response.CreateShopOrderResponse
import com.markettwits.cloud_shop.model.orders.UserOrdersRemote
import com.markettwits.core_cloud.provider.HttpClientProvider
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.parameters
import kotlinx.serialization.encodeToString

class SportSauceShopOrderApiBase(
    httpClient: HttpClientProvider,
    isLoggerEnabled: Boolean = true,
) : SportSauceShopOrderApi {

    private val json = httpClient.json()

    private val client = httpClient.provide(isLoggerEnabled)

    override suspend fun createOrder(
        request: CreateShopOrderRequest,
        token: String
    ): CreateShopOrderResponse {
        val response = client.post("order") {
            setBody(request)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        return json.decodeFromString(response.body())
    }

    override suspend fun checkOrder(
        request: CheckShopOrderRequest,
        token: String
    ): List<CheckShopOrderResponseItem> {
        val response = client.post("order/check"){
            setBody(request)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        return json.decodeFromString(response.body())
    }

    override suspend fun fetchUserOrders(
        skipCount: Int,
        maxResultCount: Int,
        userId : Int,
        token: String
    ): List<Order> {
        val response = client.post("order/user-history/$userId"){
            parameters {
                append("skipCount", skipCount.toString())
                append("maxResultCount", maxResultCount.toString())
            }
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        return json.decodeFromString<UserOrdersRemote>(response.body()).rows
    }
}
