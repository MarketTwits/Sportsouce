package com.markettwits.start.data.registration


import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.start.domain.StartPromo
import com.markettwits.start.domain.StartRegistryResult
import com.markettwits.start.presentation.order.domain.OrderStatement

interface RegistrationStartRepository {
    suspend fun loadOrder(
        startTitle: String,
        distanceInfo: DistanceItem,
        paymentDisabled: Boolean,
        paymentType: String,
    ): Result<OrderStatement>

    suspend fun promo(value: String, startId: Int): Result<StartPromo>

    suspend fun registry(
        withoutPayment: Boolean,
        statement: OrderStatement,
        distanceInfo: DistanceItem,
        startId: Int
    ): StartRegistryResult
}