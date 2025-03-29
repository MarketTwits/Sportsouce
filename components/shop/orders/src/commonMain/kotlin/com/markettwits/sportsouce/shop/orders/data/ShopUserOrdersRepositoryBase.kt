package com.markettwits.sportsouce.shop.orders.data

import com.markettwits.sportsouce.auth.service.api.AuthDataSource
import com.markettwits.sportsouce.shop.cloud.api.SportSauceShopOrderApi
import com.markettwits.sportsouce.shop.orders.data.mapper.ShopUserOrdersMapper
import com.markettwits.sportsouce.shop.orders.domain.ShopUserOrdersRepository
import com.markettwits.sportsouce.shop.orders.domain.models.ShopUserOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update

internal class ShopUserOrdersRepositoryBase(
    private val ordersService: SportSauceShopOrderApi,
    private val mapper: ShopUserOrdersMapper,
    private val authService: AuthDataSource
) : ShopUserOrdersRepository {

    private val cache = MutableStateFlow<Result<List<ShopUserOrder>>?>(null)

    override suspend fun getUserOrders(forced: Boolean): Flow<List<ShopUserOrder>> =
        if (forced) {
            refreshUserOrders()
        } else
            flow {
                val cachedData = cache.first()
                if (cachedData != null) {
                    emit(cachedData.getOrElse { emptyList() })
                } else {
                    val result = fetchAndCacheOrders()
                    emit(result.getOrElse { emptyList() })
                }
            }

    private suspend fun fetchAndCacheOrders(): Result<List<ShopUserOrder>> {

        val token = authService.updateToken().getOrThrow()
        val userId = authService.userID().getOrThrow()
        val items = ordersService.fetchUserOrders(token = token, userId = userId)
        val mappedOrders = mapper.mapToUserOrders(items)

        val result = Result.success(mappedOrders)
        cache.update { result }
        return result
    }

    private fun refreshUserOrders(): Flow<List<ShopUserOrder>> = flow {
        val result = fetchAndCacheOrders()
        emit(result.getOrElse { emptyList() })
    }

}