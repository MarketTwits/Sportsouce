package com.markettwits.shop.orders.data

import com.markettwits.cloud_shop.api.SportSauceShopOrderApi
import com.markettwits.profile.api.AuthDataSource
import com.markettwits.shop.orders.data.mapper.mapToUserOrders
import com.markettwits.shop.orders.domain.ShopUserOrdersRepository
import com.markettwits.shop.orders.domain.models.ShopUserOrder
import kotlinx.coroutines.runBlocking

class ShopUserOrdersRepositoryBase(
    private val ordersService: SportSauceShopOrderApi,
    private val authService : AuthDataSource
) : ShopUserOrdersRepository {

    override suspend fun getUserOrders(): Result<List<ShopUserOrder>> = runCatching {
        val token = authService.updateToken().getOrThrow()
        val userId = authService.userID().getOrThrow()
        ordersService.fetchUserOrders(token = token, userId = userId).mapToUserOrders()
    }

}