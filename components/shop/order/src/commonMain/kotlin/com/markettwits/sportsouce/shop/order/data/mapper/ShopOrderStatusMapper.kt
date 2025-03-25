package com.markettwits.sportsouce.shop.order.data.mapper

import com.markettwits.sportsouce.shop.cloud.model.orders.OrderStatus
import com.markettwits.sportsouce.shop.order.domain.model.ShopOrderStatus


fun OrderStatus?.shopOrderStatusMapper() : ShopOrderStatus =
     when(this){
        OrderStatus.CONFIRMED -> ShopOrderStatus.Confirmed
        OrderStatus.COMPLETED -> ShopOrderStatus.Completed
        OrderStatus.PENDING -> ShopOrderStatus.Pending
        OrderStatus.CANCELLED -> ShopOrderStatus.Cancelled
        null -> ShopOrderStatus.Empty
}