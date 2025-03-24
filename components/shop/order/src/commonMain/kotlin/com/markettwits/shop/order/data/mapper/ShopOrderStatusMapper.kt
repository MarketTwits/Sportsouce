package com.markettwits.shop.order.data.mapper

import com.markettwits.cloud_shop.model.orders.OrderStatus
import com.markettwits.shop.order.domain.model.ShopOrderStatus


fun OrderStatus?.shopOrderStatusMapper() : ShopOrderStatus =
     when(this){
        OrderStatus.CONFIRMED -> ShopOrderStatus.Confirmed
        OrderStatus.COMPLETED -> ShopOrderStatus.Completed
        OrderStatus.PENDING -> ShopOrderStatus.Pending
        OrderStatus.CANCELLED -> ShopOrderStatus.Cancelled
        null -> ShopOrderStatus.Empty
}