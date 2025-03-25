package com.markettwits.sportsouce.shop.order.domain.model

sealed interface ShopPaymentType {

    fun isAvailable(shopDeliveryType: ShopDeliveryType) : Boolean

    data object Cache : ShopPaymentType {
        override fun isAvailable(shopDeliveryType: ShopDeliveryType) : Boolean =
             shopDeliveryType !is ShopDeliveryType.Delivery

        override fun toString(): String  = "При получении"
    }

    data object Online : ShopPaymentType {
        override fun isAvailable(shopDeliveryType: ShopDeliveryType): Boolean = true

        override fun toString(): String = "Онлайн"
    }

    interface Factory{
        fun getShopPaymentType(value : String) : ShopPaymentType

        fun getTitle(shopPaymentType: ShopPaymentType) : String
    }

}