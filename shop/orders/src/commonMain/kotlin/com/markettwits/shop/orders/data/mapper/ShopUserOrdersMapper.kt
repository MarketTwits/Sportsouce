package com.markettwits.shop.orders.data.mapper

import com.markettwits.cloud_shop.model.order.common.Order
import com.markettwits.shop.order.data.mapper.ShopDeliveryTypeFactory
import com.markettwits.shop.order.data.mapper.ShopPaymentTypeFactory
import com.markettwits.shop.order.domain.model.ShopOrder
import com.markettwits.shop.orders.domain.models.ShopItemOrder
import com.markettwits.shop.orders.domain.models.ShopUserOrder

internal fun List<Order>.mapToUserOrders() : List<ShopUserOrder> = map {
    ShopUserOrder(
        order = ShopOrder(
            cost = it.cost,
            externalId = it.externalId,
            id = it.id,
            internalId = it.internalId,
            paidOnline = it.paidOnline,
            createAt = it.createdAt,
            paymentMethod = ShopPaymentTypeFactory.getShopPaymentType(it.paymentMethod),
            shippingMethod = ShopDeliveryTypeFactory.getShopDeliveryType(it.shippingMethod),
        ),
        items = it.orderItems?.map { item ->
            ShopItemOrder(
                brand = item.brand,
                code = item.code,
                description = item.description,
                discountPrice = item.discountPrice,
                id = item.id,
                images = item.images.map { image -> image.file?.fullPath ?: "" },
                model = item.model,
                name = item.name,
                orderProduct = ShopItemOrder.CostWithCount(
                    cost = item.orderProduct.cost,
                    count = item.orderProduct.count
                ),
                price = item.price
            )
        } ?: emptyList()
    )
}