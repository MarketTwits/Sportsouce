package com.markettwits.start.register.presentation.order.domain.interactor

import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.start.register.domain.StartRegistryResult
import com.markettwits.start.register.presentation.order.domain.OrderStatement

interface OrderInteractor {
    suspend fun registry(
        orderStatement: OrderStatement,
        distanceItem: DistanceItem,
        startId: Int
    ): StartRegistryResult

    suspend fun valid(orderStatement: OrderStatement): Result<OrderStatement>

    suspend fun order(
        startTitle: String,
        distanceInfo: DistanceItem,
        paymentDisabled: Boolean,
        paymentType: String
    ): Result<OrderStatement>
}