package com.markettwits.shop.orders.data.mapper

import com.markettwits.cloud_shop.model.order.common.Order
import com.markettwits.shop.domain.mapper.mapCloudPriceToDouble
import com.markettwits.shop.order.data.mapper.ShopDeliveryTypeFactory
import com.markettwits.shop.order.data.mapper.ShopPaymentTypeFactory
import com.markettwits.shop.order.data.mapper.shopOrderStatusMapper
import com.markettwits.shop.order.domain.model.ShopOrder
import com.markettwits.shop.order.domain.model.ShopOrderStatus
import com.markettwits.shop.orders.domain.models.ShopUserItemOrder
import com.markettwits.shop.orders.domain.models.ShopUserOrder
import com.markettwits.time.TimeMapper
import com.markettwits.time.TimePattern

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
                shippingMethod = ShopDeliveryTypeFactory.getShopDeliveryType(it.shippingMethod),
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

