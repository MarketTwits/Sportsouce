package com.markettwits.sportsouce.shop.orders.data.mapper

import com.markettwits.core.time.TimeMapper
import com.markettwits.core.time.TimePattern
import com.markettwits.sportsouce.shop.cloud.model.order.common.Order
import com.markettwits.sportsouce.shop.domain.mapper.mapCloudPriceToDouble
import com.markettwits.sportsouce.shop.order.data.mapper.ShopDeliveryTypeFactory
import com.markettwits.sportsouce.shop.order.data.mapper.ShopPaymentTypeFactory
import com.markettwits.sportsouce.shop.order.data.mapper.shopOrderStatusMapper
import com.markettwits.sportsouce.shop.order.domain.model.ShopOrder
import com.markettwits.sportsouce.shop.orders.domain.models.ShopUserItemOrder
import com.markettwits.sportsouce.shop.orders.domain.models.ShopUserOrder

internal class ShopUserOrdersMapper(private val timeMapper: TimeMapper){

    fun mapToUserOrders(items : List<Order>) : List<ShopUserOrder> = items.map {
        ShopUserOrder(
            order = ShopOrder(
                cost = mapCloudPriceToDouble(it.cost),
                externalId = it.externalId,
                id = it.id,
                internalId = it.internalId,
                paidOnline = it.paidOnline,
                createAt = timeMapper.mapTime(TimePattern.FullWithEmptySpace, it.createdAt),
                paymentMethod = ShopPaymentTypeFactory.getShopPaymentType(it.paymentMethod),
                shippingMethod = ShopDeliveryTypeFactory.getShopDeliveryType(it.shippingMethod?: ""),
                status = it.status.shopOrderStatusMapper()
            ),
            items = it.orderItems?.map { item ->
                ShopUserItemOrder(
                    brand = item.brand,
                    code = item.code,
                    description = item.description,
                    discountPrice = item.discountPrice,
                    id = item.id,
                    images = item.images.map { image -> image.file?.fullPath ?: "" },
                    model = item.model,
                    name = item.name,
                    orderProduct = ShopUserItemOrder.CostWithCount(
                        cost = mapCloudPriceToDouble(item.orderProduct.cost),
                        count = item.orderProduct.count
                    ),
                    price = mapCloudPriceToDouble(item.price)
                )
            } ?: emptyList()
        )
    }
}

