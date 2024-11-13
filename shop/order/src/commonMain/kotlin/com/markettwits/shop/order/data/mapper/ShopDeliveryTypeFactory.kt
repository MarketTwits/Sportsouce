package com.markettwits.shop.order.data.mapper

import com.markettwits.cloud_shop.model.order.request.DeliveryMethod
import com.markettwits.shop.order.domain.model.ShopDeliveryType

object ShopDeliveryTypeFactory : ShopDeliveryType.Factory {
    override fun getShopDeliveryType(value: String): ShopDeliveryType =
         when (value.lowercase()) {
            DeliveryMethod.DELIVERY.name.lowercase() -> ShopDeliveryType.Delivery
            DeliveryMethod.PICKUP.name.lowercase() -> ShopDeliveryType.Pickup
            else -> throw IllegalArgumentException("Unknown ShopPaymentType for value: $value")
    }
}