package com.markettwits.shop.order.domain.model

import com.markettwits.shop.order.domain.model.ShopOrderResult.Companion.BASE_ORDER_PAGE_URL


data class ShopOrderResult(
    val errors: List<Error>,
    val order: ShopOrder? = null,
    val payment: Payment? = null
) {

    data class Payment(
        val formUrl: String,
        val orderId: String
    )

    data class Error(
        val existingQuantity: Int,
        val requestedQuantity: Int,
        val partlyExists: Boolean,
        val message: String,
        val productId: String,
    )

    companion object{
        const val BASE_ORDER_PAGE_URL =
            "https://sportsauce.ru/order/success?orderId"

    }
}

internal fun ShopOrderResult.getUrlAfterCreateOrder() : String{
    return if (order != null){
            if (order.paidOnline)
                 payment?.formUrl ?: order.getOrderDetailUrl()
            else
                 order.getOrderDetailUrl()
    } else
        throw IllegalStateException("Ошибка формирвоания заказа")
}

internal fun ShopOrder.getOrderDetailUrl() : String =
    "$BASE_ORDER_PAGE_URL=$internalId"

