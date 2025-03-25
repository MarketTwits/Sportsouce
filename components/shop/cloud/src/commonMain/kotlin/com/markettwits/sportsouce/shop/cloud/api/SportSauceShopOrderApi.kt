package com.markettwits.sportsouce.shop.cloud.api

import com.markettwits.sportsouce.shop.cloud.model.check.request.CheckShopOrderRequest
import com.markettwits.sportsouce.shop.cloud.model.check.response.CheckShopOrderResponseItem
import com.markettwits.sportsouce.shop.cloud.model.order.common.Order
import com.markettwits.sportsouce.shop.cloud.model.order.request.CreateShopOrderRequest
import com.markettwits.sportsouce.shop.cloud.model.order.response.CreateShopOrderResponse

interface SportSauceShopOrderApi {

    suspend fun createOrder(
        request: CreateShopOrderRequest,
        token : String
    ) : CreateShopOrderResponse

    suspend fun checkOrder(
        request : CheckShopOrderRequest,
        token : String
    ) : List<CheckShopOrderResponseItem>

    suspend fun fetchUserOrders(
        skipCount : Int = 0 ,
        maxResultCount : Int = 100,
        userId : Int,
        token: String
    ) : List<Order>

}