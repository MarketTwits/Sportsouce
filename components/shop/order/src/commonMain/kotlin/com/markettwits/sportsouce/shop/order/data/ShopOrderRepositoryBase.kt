package com.markettwits.sportsouce.shop.order.data

import com.markettwits.sportsouce.auth.service.api.AuthDataSource
import com.markettwits.sportsouce.shop.cart.domain.ShopItemCart
import com.markettwits.sportsouce.shop.cloud.api.SportSauceShopApi
import com.markettwits.sportsouce.shop.cloud.api.SportSauceShopOrderApi
import com.markettwits.sportsouce.shop.order.data.mapper.mapCheckOrderRequest
import com.markettwits.sportsouce.shop.order.data.mapper.mapCreateOrderRequest
import com.markettwits.sportsouce.shop.order.data.mapper.mapCreateShopOrderResult
import com.markettwits.sportsouce.shop.order.domain.ShopOrderRepository
import com.markettwits.sportsouce.shop.order.domain.model.ShopDeliveryType
import com.markettwits.sportsouce.shop.order.domain.model.ShopItemOrderResult
import com.markettwits.sportsouce.shop.order.domain.model.ShopOrderResult
import com.markettwits.sportsouce.shop.order.domain.model.ShopPaymentType
import com.markettwits.sportsouce.shop.order.domain.model.ShopRecipient

class ShopOrderRepositoryBase(
    private val shopServiceProductApi: SportSauceShopApi,
    private val shopServiceOrderApi: SportSauceShopOrderApi,
    private val authService: AuthDataSource
) : ShopOrderRepository {

    override suspend fun createOrder(
        deliveryType: ShopDeliveryType,
        paymentType: ShopPaymentType,
        shopItems: List<ShopItemCart>,
        recipient: ShopRecipient
    ): Result<ShopOrderResult> = runCatching {

        val user = authService.sharedUser().getOrThrow()

        val token = authService.updateToken().getOrThrow()

        val cloudItems = shopItems.map {
            shopServiceProductApi.product(it.item.id)
        }

        val request = mapCreateOrderRequest(
            userId = user.id,
            products = cloudItems,
            orderProducts = shopItems,
            paymentType = paymentType,
            deliveryType = deliveryType
        )

        shopServiceOrderApi.createOrder(
            request = request,
            token = token
        ).mapCreateShopOrderResult()
    }

    override suspend fun checkOrder(shopItems: List<ShopItemCart>) : Result<List<ShopItemOrderResult>> = runCatching {
        val token = authService.updateToken().getOrThrow()
        shopServiceOrderApi.checkOrder(
            request = shopItems.mapCheckOrderRequest {
                shopServiceProductApi.product(it)
            },
            token = token
        ).mapCheckOrderRequest(shopItems)
    }


    override suspend fun getUserInfo(): ShopRecipient {
        val user = authService.sharedUser().getOrThrow()
        return ShopRecipient(
            name = user.name,
            phone = user.phone
        )
    }
}