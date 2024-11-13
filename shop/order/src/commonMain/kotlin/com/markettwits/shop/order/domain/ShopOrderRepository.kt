package com.markettwits.shop.order.domain

import com.markettwits.cloud_shop.model.order.response.CreateShopOrderResponse
import com.markettwits.shop.cart.domain.ShopItemCart
import com.markettwits.shop.order.domain.model.ShopDeliveryType
import com.markettwits.shop.order.domain.model.ShopItemOrderResult
import com.markettwits.shop.order.domain.model.ShopOrderResult
import com.markettwits.shop.order.domain.model.ShopPaymentType
import com.markettwits.shop.order.domain.model.ShopRecipient

interface ShopOrderRepository {

    suspend fun createOrder(
        deliveryType: ShopDeliveryType,
        paymentType: ShopPaymentType,
        shopItems : List<ShopItemCart>,
        recipient: ShopRecipient
    ) : Result<ShopOrderResult>

    suspend fun checkOrder(shopItems: List<ShopItemCart>) : Result<List<ShopItemOrderResult>>

    suspend fun getUserInfo() : ShopRecipient

}