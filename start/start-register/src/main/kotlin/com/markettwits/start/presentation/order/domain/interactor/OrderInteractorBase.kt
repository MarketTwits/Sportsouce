package com.markettwits.start.presentation.order.domain.interactor

import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.start.data.registration.RegistrationStartRepository
import com.markettwits.start.domain.StartRegistryResult
import com.markettwits.start.presentation.order.domain.OrderStatement
import com.markettwits.start.presentation.order.domain.validation.OrderValidation

class OrderInteractorBase(
    private val repository: RegistrationStartRepository,
    private val orderValidation: OrderValidation
) : OrderInteractor {
    override suspend fun registry(
        orderStatement: OrderStatement,
        distanceItem: DistanceItem,
        startId: Int
    ): StartRegistryResult =
        repository.registry(
            withoutPayment = !orderStatement.payNow,
            statement = orderStatement,
            distanceInfo = distanceItem,
            startId = startId
        )

    override suspend fun valid(orderStatement: OrderStatement): Result<OrderStatement> =
        orderValidation.validate(orderStatement)

    override suspend fun order(
        distanceInfo: DistanceItem,
        paymentDisabled: Boolean,
        paymentType: String
    ): Result<OrderStatement> =
        repository.loadOrder(distanceInfo, paymentDisabled, paymentType)

}
