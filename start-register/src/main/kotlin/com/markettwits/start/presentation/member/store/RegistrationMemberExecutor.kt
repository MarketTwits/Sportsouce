package com.markettwits.start.presentation.member.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.start.domain.StartStatement
import com.markettwits.start.presentation.member.domain.RegistrationMemberValidator
import com.markettwits.start.presentation.member.store.RegistrationMemberStore.Intent
import com.markettwits.start.presentation.member.store.RegistrationMemberStore.Label
import com.markettwits.start.presentation.member.store.RegistrationMemberStore.Message
import com.markettwits.start.presentation.member.store.RegistrationMemberStore.State

class RegistrationMemberExecutor(private val validation: RegistrationMemberValidator) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.ChangeFiled -> dispatch(Message.OnValueChanged(intent.startStatement))
            is Intent.OnClickContinue -> apply(getState().value)
            is Intent.Pop -> publish(Label.OnClickPop)
            is Intent.OnConsumedEvent -> dispatch(Message.OnConsumedEvent)
        }
    }

    private fun apply(startStatement: StartStatement) {
        validation.validateFields(startStatement).fold(
            onSuccess = {
                publish(Label.OnClickContinue(startStatement))
            }, onFailure = {
                dispatch(Message.ShowEvent(it.message.toString()))
            })
    }
}
