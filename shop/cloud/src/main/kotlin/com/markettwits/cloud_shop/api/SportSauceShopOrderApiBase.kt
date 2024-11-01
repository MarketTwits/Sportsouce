package com.markettwits.cloud_shop.api

import com.markettwits.cloud_shop.model.order.request.CreateShopOrderRequest
import com.markettwits.cloud_shop.model.order.response.CreateShopOrderResponse
import com.markettwits.core_cloud.provider.HttpClientProvider
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders

class SportSauceShopOrderApiBase(
    httpClient: HttpClientProvider,
    isLoggerEnabled: Boolean = false,
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
}