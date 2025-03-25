package com.markettwits.sportsouce.shop.order.data.mapper

import com.markettwits.sportsouce.shop.cloud.model.order.request.PaymentMethod
import com.markettwits.sportsouce.shop.order.domain.model.ShopPaymentType

object ShopPaymentTypeFactory : ShopPaymentType.Factory {

    override fun getShopPaymentType(value: String): ShopPaymentType =
         when (value.lowercase()) {
            PaymentMethod.ONLINE.name.lowercase() -> ShopPaymentType.Online
            PaymentMethod.ON_RECEIPT.name.lowercase() -> ShopPaymentType.Cache
            else -> throw IllegalArgumentException("Unknown ShopPaymentType for value: $value")
    }

    override fun getTitle(shopPaymentType: ShopPaymentType): String =
        when(shopPaymentType){
            is ShopPaymentType.Cache -> shopPaymentType.toString()
            is ShopPaymentType.Online -> shopPaymentType.toString()
        }

}