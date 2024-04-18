package com.markettwits.start.register.presentation.order.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.start.register.presentation.order.presentation.store.OrderStore.Message
import com.markettwits.start.register.presentation.order.presentation.store.OrderStore.State

object OrderStoreReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.PreloadFailed -> State(isError = true, message = msg.message)
            is Message.PreloadLoading -> State(isLoading = true)
            is Message.PreloadSuccess -> State(orderStatement = msg.startStatement)
            is Message.UpdateState -> msg.state
        }
    }
}