package com.markettwits.cloud_shop.api

import com.markettwits.cloud_shop.model.check.request.CheckShopOrderRequest
import com.markettwits.cloud_shop.model.check.response.CheckShopOrderResponseItem
import com.markettwits.cloud_shop.model.order.common.Order
import com.markettwits.cloud_shop.model.order.request.CreateShopOrderRequest
import com.markettwits.cloud_shop.model.order.response.CreateShopOrderResponse

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