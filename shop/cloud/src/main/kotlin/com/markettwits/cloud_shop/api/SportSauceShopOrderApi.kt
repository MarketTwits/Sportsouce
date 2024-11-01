package com.markettwits.cloud_shop.api

import com.markettwits.cloud_shop.model.order.request.CreateShopOrderRequest
import com.markettwits.cloud_shop.model.order.response.CreateShopOrderResponse

interface SportSauceShopOrderApi {

    suspend fun createOrder(
        request: CreateShopOrderRequest,
        token : String
    ) : CreateShopOrderResponse

}