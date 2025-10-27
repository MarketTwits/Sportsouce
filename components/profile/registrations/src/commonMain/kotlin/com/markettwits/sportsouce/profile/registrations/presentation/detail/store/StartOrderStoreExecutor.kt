package com.markettwits.sportsouce.profile.registrations.presentation.detail.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.IntentAction
import com.markettwits.core.log.LogTagProvider
import com.markettwits.core.log.errorLog
import com.markettwits.sportsouce.profile.registrations.data.StartOrderRegistrationRepository
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.presentation.detail.store.StartOrderStore.*
import com.markettwits.sportsouce.profile.registrations.presentation.detail.store.StartOrderStore.Label.OnClickStart
import kotlinx.coroutines.launch

class StartOrderStoreExecutor(
    private val repository: StartOrderRegistrationRepository,
    private val intentAction: IntentAction
) : CoroutineExecutor<Intent, Unit, State, Message, Label>(), LogTagProvider {

    override val tag: String = "StartOrderStoreExecutor (Detail)"

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.Dismiss -> publish(Label.Dismiss)
            is Intent.OnClickPay -> getPaymentUrl(intent.orderId)
            is Intent.OnClickStart -> publish(OnClickStart(intent.startId))
            is Intent.OnClickUpdatePrice -> getActualPrice(state().startOrderInfo)
            is Intent.OnClickHelp -> intentAction.openWebPage("https://t.me/sportsoyuznsk")
        }
    }

    override fun executeAction(action: Unit) {
        getActualPrice(state().startOrderInfo)
    }

    private fun getActualPrice(orderInfo: StartOrderInfo) {
        scope.launch {
            if (orderInfo.payment.isPaid) {
                dispatch(Message.GetPriceDontRequired)
            } else {
                dispatch(Message.GetPriceLoading)
                repository.getPrice(
                    orderInfo.id,
                    orderInfo.members.map { it.distanceName },
                    orderInfo.startId
                ).fold(onSuccess = {
                    dispatch(Message.GetPriceSuccess(it.totalPrice))
                }, onFailure = {
                    dispatch(Message.GetPriceFailed(it.message.toString()))
                    errorLog(it) { "Failed when try get price for order ${orderInfo.id}" }
                })
            }
        }
    }

    private fun getPaymentUrl(id: Int) {
        scope.launch {
            dispatch(Message.GetPriceLoading)
            repository.pay(id).fold(
                onSuccess = {
                    dispatch(Message.GetPriceSuccess(state().startOrderInfo.cost))
                    intentAction.openWebPage(it)
                },
                onFailure = {
                    dispatch(Message.GetPriceFailed(it.message.toString()))
                }
            )
        }
    }
}
