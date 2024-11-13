package com.markettwits.shop.order.data.mapper

import com.markettwits.cloud_shop.model.order.request.PaymentMethod
import com.markettwits.shop.order.domain.model.ShopPaymentType

object ShopPaymentTypeFactory : ShopPaymentType.Factory {
    override fun getShopPaymentType(value: String): ShopPaymentType  =
         when (value.lowercase()) {
            PaymentMethod.ONLINE.name.lowercase() -> ShopPaymentType.Online
            PaymentMethod.ON_RECEIPT.name.lowercase() -> ShopPaymentType.Cache
            else -> throw IllegalArgumentException("Unknown ShopPaymentType for value: $value")
    }
}