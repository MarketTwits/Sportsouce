package com.markettwits.shop.order.data.mapper

import com.markettwits.cloud_shop.model.order.request.CreateShopOrderRequest
import com.markettwits.cloud_shop.model.order.request.DeliveryMethod
import com.markettwits.cloud_shop.model.order.request.PaymentMethod
import com.markettwits.cloud_shop.model.order.request.ShopOrderItemWithCount
import com.markettwits.cloud_shop.model.order.request.ShopOrderItemWithPrice
import com.markettwits.cloud_shop.model.product.ProductRemote
import com.markettwits.shop.cart.domain.ShopItemCart
import com.markettwits.shop.order.domain.model.ShopDeliveryType
import com.markettwits.shop.order.domain.model.ShopPaymentType

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