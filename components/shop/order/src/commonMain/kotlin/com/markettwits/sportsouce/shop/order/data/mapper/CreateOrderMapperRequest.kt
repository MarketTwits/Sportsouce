package com.markettwits.sportsouce.shop.order.data.mapper

import com.markettwits.sportsouce.shop.cart.domain.ShopItemCart
import com.markettwits.sportsouce.shop.cloud.model.order.request.CreateShopOrderRequest
import com.markettwits.sportsouce.shop.cloud.model.order.request.DeliveryMethod
import com.markettwits.sportsouce.shop.cloud.model.order.request.PaymentMethod
import com.markettwits.sportsouce.shop.cloud.model.order.request.ShopOrderItemWithCount
import com.markettwits.sportsouce.shop.cloud.model.order.request.ShopOrderItemWithPrice
import com.markettwits.sportsouce.shop.cloud.model.product.ProductRemote
import com.markettwits.sportsouce.shop.order.domain.model.ShopDeliveryType
import com.markettwits.sportsouce.shop.order.domain.model.ShopPaymentType

internal fun ShopPaymentType.mapToCloud() : PaymentMethod =
    when(this){
        ShopPaymentType.Cache -> PaymentMethod.ON_RECEIPT
        ShopPaymentType.Online -> PaymentMethod.ONLINE
    }

internal fun ShopDeliveryType.mapToCloud() : DeliveryMethod =
    when(this){
        ShopDeliveryType.Delivery -> DeliveryMethod.DELIVERY
        ShopDeliveryType.Pickup -> DeliveryMethod.PICKUP
    }

internal fun mapCreateOrderRequest(
    userId : Int,
    products : List<ProductRemote>,
    orderProducts : List<ShopItemCart>,
    paymentType: ShopPaymentType,
    deliveryType: ShopDeliveryType
) : CreateShopOrderRequest =
    CreateShopOrderRequest(
        userId = userId,
        items = products.map {
            ShopOrderItemWithCount(
                count = orderProducts.find { item -> item.item.id == it.product.id }?.count
                    ?: throw NoSuchElementException("No such element exception"),
                product = ShopOrderItemWithPrice(
                    price = it.product.price,
                    product = it.product,
                    productId = it.product.id
                )
            )
        },
        paymentMethod = paymentType.mapToCloud(),
        deliveryMethod = deliveryType.mapToCloud()
    )