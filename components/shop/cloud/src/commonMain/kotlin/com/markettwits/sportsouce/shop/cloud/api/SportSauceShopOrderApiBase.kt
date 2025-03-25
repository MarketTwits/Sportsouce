package com.markettwits.sportsouce.shop.cloud.api

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.sportsouce.shop.cloud.model.check.request.CheckShopOrderRequest
import com.markettwits.sportsouce.shop.cloud.model.check.response.CheckShopOrderResponseItem
import com.markettwits.sportsouce.shop.cloud.model.order.common.Order
import com.markettwits.sportsouce.shop.cloud.model.order.request.CreateShopOrderRequest
import com.markettwits.sportsouce.shop.cloud.model.order.response.CreateShopOrderResponse
import com.markettwits.sportsouce.shop.cloud.model.orders.UserOrdersRemote
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.parameters

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
        val response = client.get("order/user-history/$userId"){
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
