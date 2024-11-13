package com.markettwits.shop.order.domain.model

sealed interface ShopPaymentType {

    fun isAvailable(shopDeliveryType: ShopDeliveryType) : Boolean

    data object Cache : ShopPaymentType {
        override fun isAvailable(shopDeliveryType: ShopDeliveryType) : Boolean =
             shopDeliveryType !is ShopDeliveryType.Delivery
    }

    data object Online : ShopPaymentType {
        override fun isAvailable(shopDeliveryType: ShopDeliveryType): Boolean = true
    }

    interface Factory{
        fun getShopPaymentType(value : String) : ShopPaymentType
    }

}