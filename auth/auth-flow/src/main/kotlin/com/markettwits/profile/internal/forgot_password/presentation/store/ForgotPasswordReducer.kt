package com.markettwits.profile.internal.forgot_password.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.core_ui.event.EventContent
import com.markettwits.core_ui.event.consumed
import com.markettwits.core_ui.event.triggered
import com.markettwits.profile.internal.forgot_password.presentation.store.ForgotPasswordStore.Message
import com.markettwits.profile.internal.forgot_password.presentation.store.ForgotPasswordStore.State

internal object ForgotPasswordReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.Failed -> copy(
                isLoading = false,
                event = triggered(EventContent(false, msg.message))
            )

            is Message.Loading -> copy(isLoading = true)
            is Message.OnConsumedEvent -> copy(event = consumed())
            is Message.OnValueChanged -> copy(email = msg.email)
            is Message.Success -> copy(
                event = triggered(EventContent(true, msg.message)),
                isLoading = false
            )
        }
    }
}