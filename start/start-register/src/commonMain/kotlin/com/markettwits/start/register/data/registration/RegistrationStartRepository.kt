package com.markettwits.start.register.data.registration


import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.start.register.domain.StartPromo
import com.markettwits.start.register.domain.StartRegistryResult
import com.markettwits.start.register.presentation.order.domain.OrderStatement

interface RegistrationStartRepository {

    suspend fun loadOrder(
        startTitle: String,
        distanceInfo: DistanceItem,
        discounts: List<DistanceItem.Discount>,
        paymentDisabled: Boolean,
        paymentType: String,
    ): Result<OrderStatement>

    suspend fun promo(value: String, startId: Int,distancesId : List<Int>): Result<StartPromo>

    suspend fun registry(
        withoutPayment: Boolean,
        statement: OrderStatement,
        distanceInfo: DistanceItem,
        startId: Int
    ): StartRegistryResult
}