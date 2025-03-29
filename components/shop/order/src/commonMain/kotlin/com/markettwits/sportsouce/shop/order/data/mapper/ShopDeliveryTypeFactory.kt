package com.markettwits.sportsouce.shop.order.data.mapper

import com.markettwits.sportsouce.shop.cloud.model.order.request.DeliveryMethod
import com.markettwits.sportsouce.shop.order.domain.model.ShopDeliveryType

object ShopDeliveryTypeFactory : ShopDeliveryType.Factory {
    override fun getShopDeliveryType(value: String): ShopDeliveryType =
         when (value.lowercase()) {
            DeliveryMethod.DELIVERY.name.lowercase() -> ShopDeliveryType.Delivery
            DeliveryMethod.PICKUP.name.lowercase() -> ShopDeliveryType.Pickup
            else -> ShopDeliveryType.Pickup
    }

    override fun getTitle(shopDeliveryType: ShopDeliveryType): String  =
        when(shopDeliveryType){
            is ShopDeliveryType.Delivery -> shopDeliveryType.toString()
            is ShopDeliveryType.Pickup -> shopDeliveryType.toString()
        }
}