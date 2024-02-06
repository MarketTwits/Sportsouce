package com.markettwits.start.presentation.order.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.cloud.model.auth.common.AuthErrorResponse
import com.markettwits.start.data.registration.RegistrationStartRepository
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.launch

class OrderBootstrapper(
    private val repository: RegistrationStartRepository,
    private val distanceInfo: DistanceItem,
    private val paymentDisabled: Boolean
) : CoroutineBootstrapper<OrderStore.Action>() {
    override fun invoke() {
        launch(distanceInfo, paymentDisabled)
    }

    private fun launch(distanceInfo: DistanceItem, paymentDisabled: Boolean) {
        dispatch(OrderStore.Action.Loading)
        scope.launch {
            repository.loadOrder(distanceInfo, paymentDisabled).fold(
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