package com.markettwits.shop.order.domain.model

sealed interface ShopDeliveryType {

    data object Pickup : ShopDeliveryType{

        override fun toString(): String = "Самовывоз"

    }

    data object Delivery : ShopDeliveryType{
        override fun toString(): String  = "Доставка"
    }

    interface Factory{

        fun getShopDeliveryType(value : String) : ShopDeliveryType

        fun getTitle(shopDeliveryType: ShopDeliveryType) : String
    }

}