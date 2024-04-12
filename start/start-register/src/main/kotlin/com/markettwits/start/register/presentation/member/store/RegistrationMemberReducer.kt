package com.markettwits.start.register.presentation.member.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.core_ui.event.EventContent
import com.markettwits.core_ui.event.consumed
import com.markettwits.core_ui.event.triggered
import com.markettwits.start.register.presentation.member.store.RegistrationMemberStore.Message
import com.markettwits.start.register.presentation.member.store.RegistrationMemberStore.State

object RegistrationMemberReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.OnValueChanged -> copy(value = msg.startStatement)
            is Message.ShowEvent -> copy(event = triggered(EventContent(false, msg.message)))
            is Message.OnConsumedEvent -> copy(event = consumed())
        }
    }
}