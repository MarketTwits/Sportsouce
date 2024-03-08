package com.markettwits.start.presentation.order.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.cloud.model.auth.common.AuthErrorResponse
import com.markettwits.start.presentation.order.domain.interactor.OrderInteractor
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.launch

class OrderBootstrapper(
    private val firstRun: Boolean,
    private val interactor: OrderInteractor,
    private val startTitle: String,
    private val distanceInfo: DistanceItem,
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
            interactor.order(startTitle, distanceInfo, paymentDisabled, paymentType).fold(
                onSuccess = { dispatch(OrderStore.Action.InfoLoaded(it)) },
                onFailure = {
                    val message = when (it) {
                        is ClientRequestException -> it.response.body<AuthErrorResponse>().message
                        else -> it.message.toString()
                    }
                    dispatch(OrderStore.Action.InfoFailed(message))
                }
            )
        }
    }
}