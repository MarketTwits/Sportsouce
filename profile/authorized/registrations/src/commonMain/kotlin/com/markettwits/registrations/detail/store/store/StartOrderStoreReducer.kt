package com.markettwits.registrations.detail.store.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.registrations.detail.store.store.StartOrderStore.Message
import com.markettwits.registrations.detail.store.store.StartOrderStore.State

object StartOrderStoreReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.Failed -> copy(isLoading = false, isFailed = true, message = msg.message)
            is Message.Success -> copy(isLoading = false)
            is Message.Loading -> copy(isLoading = true)
        }
    }
}