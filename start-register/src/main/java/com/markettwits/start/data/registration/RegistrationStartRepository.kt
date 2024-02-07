package com.markettwits.start.data.registration


import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.start.domain.StartPromo
import com.markettwits.start.domain.StartRegistryResult
import com.markettwits.start.domain.StartStatement
import com.markettwits.start.presentation.order.domain.OrderStatement

interface RegistrationStartRepository {
    suspend fun preload(
        distanceInfo: DistanceItem,
        paymentDisabled: Boolean
    ): Result<StartStatement>

    suspend fun loadOrder(
        distanceInfo: DistanceItem,
        paymentDisabled: Boolean,
        paymentType: String,
    ): Result<OrderStatement>

    suspend fun promo(value: String, startId: Int): Result<StartPromo>
    suspend fun registry(
        withoutPayment: Boolean,
        statement: StartStatement,
        distanceInfo: DistanceItem,
        startId: Int
    ): Result<StartRegistryResult>

}