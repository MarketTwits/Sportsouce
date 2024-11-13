package com.markettwits.shop.order.domain.model

sealed interface ShopDeliveryType {

    data object Pickup : ShopDeliveryType

    data object Delivery : ShopDeliveryType

    interface Factory{
        fun getShopDeliveryType(value : String) : ShopDeliveryType
    }
}