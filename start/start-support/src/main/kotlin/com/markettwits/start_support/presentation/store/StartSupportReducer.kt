package com.markettwits.start_support.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.core_ui.event.EventContent
import com.markettwits.core_ui.event.consumed
import com.markettwits.core_ui.event.triggered
import com.markettwits.start_support.presentation.store.StartSupportStore.Message
import com.markettwits.start_support.presentation.store.StartSupportStore.State

internal object StartSupportReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.Failure -> copy(
                isError = true,
                isLoading = false,
                message = msg.message,
                eventWithContent = triggered(
                    EventContent(false, msg.message)
                )
            )

            is Message.Loading -> copy(isLoading = true, isError = false)
            is Message.OnValueChanged -> copy(cost = msg.value)
            is Message.Success -> copy(
                isSuccess = true,
                isLoading = false,
                message = msg.url,
                eventWithContent = triggered(
                    EventContent(true, msg.url)
                )
            )

            is Message.OnConsumedEvent -> copy(eventWithContent = consumed())
        }
    }
}