package com.markettwits.registrations.start_order_detail.store.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.registrations.registrations_list.data.StartOrderRegistrationRepository
import com.markettwits.registrations.start_order_detail.store.store.StartOrderStore.Intent
import com.markettwits.registrations.start_order_detail.store.store.StartOrderStore.Label
import com.markettwits.registrations.start_order_detail.store.store.StartOrderStore.Message
import com.markettwits.registrations.start_order_detail.store.store.StartOrderStore.State
import kotlinx.coroutines.launch

class StartOrderStoreExecutor(
    private val repository: StartOrderRegistrationRepository
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.Dismiss -> publish(Label.Dismiss)
            is Intent.OnClickPay -> repay(intent.orderId)
            is Intent.OnClickStart -> publish(Label.OnClickStart(intent.startId))
        }
    }

    private fun repay(id: Int) {
        scope.launch {
            dispatch(Message.Loading)
            repository.pay(id).fold(
                onSuccess = {
                    dispatch(Message.Success(it))
                },
                onFailure = {
                    dispatch(Message.Failed(it.message.toString()))
                }
            )
        }
    }
}
