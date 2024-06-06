package com.markettwits.start.register.presentation.order.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.markettwits.cloud.exception.networkExceptionHandler
import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.start.register.presentation.order.domain.interactor.OrderInteractor
import kotlinx.coroutines.launch

class OrderBootstrapper(
    private val firstRun: Boolean,
    private val interactor: OrderInteractor,
    private val startTitle: String,
    private val distanceInfo: DistanceItem,
    private val discounts: List<DistanceItem.Discount>,
    private val paymentDisabled: Boolean,
    private val paymentType: String,
) : CoroutineBootstrapper<OrderStore.Action>() {

    override fun invoke() {
        if (firstRun)
            launch(distanceInfo, paymentDisabled, paymentType)
    }

    private fun launch(distanceInfo: DistanceItem, paymentDisabled: Boolean, paymentType: String) {
        dispatch(OrderStore.Action.Loading)
        scope.launch {
            interactor.order(startTitle, distanceInfo, discounts, paymentDisabled, paymentType)
                .fold(
                    onSuccess = {
                        dispatch(OrderStore.Action.InfoLoaded(it))
                    },
                    onFailure = {
                        dispatch(OrderStore.Action.InfoFailed(networkExceptionHandler(it).message.toString()))
                    }
                )
        }
    }
}