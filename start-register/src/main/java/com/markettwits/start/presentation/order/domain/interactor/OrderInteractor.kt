package com.markettwits.start.presentation.order.domain.interactor

import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.start.presentation.order.domain.OrderStatement

interface OrderInteractor {
    suspend fun registry(orderStatement: OrderStatement): Result<OrderStatement>
    suspend fun order(
        distanceInfo: DistanceItem,
        paymentDisabled: Boolean,
        paymentType: String
    ): Result<OrderStatement>
}