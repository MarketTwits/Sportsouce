package com.markettwits.shop.order.data.mapper

import com.markettwits.cloud_shop.model.order.common.Error
import com.markettwits.cloud_shop.model.order.common.Order
import com.markettwits.cloud_shop.model.order.common.Payment
import com.markettwits.cloud_shop.model.order.response.CreateShopOrderResponse
import com.markettwits.shop.order.domain.model.ShopOrderResult
import com.markettwits.shop.order.domain.model.ShopOrder

internal fun CreateShopOrderResponse.mapCreateShopOrderResult(): ShopOrderResult {
    return ShopOrderResult(
        errors = this.errors.map { it.toCreateShopOrderResultError() },
        order = this.order?.toCreateShopOrderResultOrder(),
        payment = this.payment?.toCreateShopOrderResultPayment()
    )
}

private fun Error.toCreateShopOrderResultError(): ShopOrderResult.Error {
    return ShopOrderResult.Error(
        existingQuantity = this.existingQuantity,
        requestedQuantity = this.requestedQuantity,
        partlyExists = this.partlyExists,
        message = this.message,
        productId = this.productId
    )
}

private fun Payment.toCreateShopOrderResultPayment(): ShopOrderResult.Payment {
    return ShopOrderResult.Payment(
        formUrl = this.formUrl,
        orderId = this.orderId
    )
}

private fun Order.toCreateShopOrderResultOrder(): ShopOrder {
    return ShopOrder(
        cost = this.cost,
        externalId = this.externalId,
        id = this.id,
        internalId = this.internalId,
        paidOnline = this.paidOnline,
        createAt = this.createdAt,
        status = this.status.shopOrderStatusMapper(),
        paymentMethod = ShopPaymentTypeFactory.getShopPaymentType(paymentMethod),
        shippingMethod = ShopDeliveryTypeFactory.getShopDeliveryType(shippingMethod)
    )
}

