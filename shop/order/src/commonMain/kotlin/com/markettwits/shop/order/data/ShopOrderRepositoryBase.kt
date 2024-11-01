package com.markettwits.shop.order.data

import com.markettwits.cloud_shop.api.SportSauceShopApi
import com.markettwits.cloud_shop.api.SportSauceShopOrderApi
import com.markettwits.cloud_shop.model.order.request.CreateShopOrderRequest
import com.markettwits.cloud_shop.model.order.request.DeliveryMethod
import com.markettwits.cloud_shop.model.order.request.PaymentMethod
import com.markettwits.cloud_shop.model.order.request.ShopOrderItem
import com.markettwits.core_ui.items.result.flatMapCallback
import com.markettwits.profile.api.AuthDataSource
import com.markettwits.shop.order.domain.ShopOrderRepository

class ShopOrderRepositoryBase(
    private val shopService: SportSauceShopApi,
    private val authService : AuthDataSource
) : ShopOrderRepository {

    override suspend fun createOrder() {

        val user = authService.userID().getOrThrow()

        val items = mutableMapOf<String, Int>()

        val request = items.map {
            shopService.product(it.key)
        }

        CreateShopOrderRequest(
            userId = user,
            items = request.map {
                ShopOrderItem(
                    it.product,
                    items[it.product.id] ?: throw NoSuchElementException()
                )
            },
            paymentMethod = PaymentMethod.ONLINE,
            deliveryMethod = DeliveryMethod.PICKUP
        )
    }
}