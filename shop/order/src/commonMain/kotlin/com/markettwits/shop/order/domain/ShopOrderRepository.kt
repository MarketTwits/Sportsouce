package com.markettwits.shop.order.domain

import com.markettwits.shop.order.domain.model.ShopRecipient

interface ShopOrderRepository {

    suspend fun createOrder()

    suspend fun getUserInfo() : ShopRecipient

}