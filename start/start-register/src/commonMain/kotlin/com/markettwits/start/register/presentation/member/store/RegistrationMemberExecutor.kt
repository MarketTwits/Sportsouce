package com.markettwits.start.register.presentation.member.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.start.register.domain.StartStatement
import com.markettwits.start.register.presentation.member.domain.RegistrationMemberValidator
import com.markettwits.start.register.presentation.member.store.RegistrationMemberStore.Intent
import com.markettwits.start.register.presentation.member.store.RegistrationMemberStore.Label
import com.markettwits.start.register.presentation.member.store.RegistrationMemberStore.Message
import com.markettwits.start.register.presentation.member.store.RegistrationMemberStore.State

class RegistrationMemberExecutor(private val validation: RegistrationMemberValidator) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.ChangeFiled -> dispatch(Message.OnValueChanged(intent.startStatement.updateAge()))
            is Intent.OnClickContinue -> apply(state().value)
            is Intent.Pop -> onClickPop(state())
            is Intent.OnConsumedEvent -> dispatch(Message.OnConsumedEvent)
            is Intent.OnClickCloseDialog -> closeDialog()
        }
    }

    private fun closeDialog() {
        dispatch(Message.ChangeAllerDialogState(false))
        publish(Label.OnClickPop)
    }

    private fun apply(startStatement: StartStatement) {
        validation.validateFields(startStatement).fold(
            onSuccess = {
                publish(Label.OnClickContinue(startStatement))
            }, onFailure = {
                dispatch(Message.ShowEvent(it.message.toString()))
            })
    }

    private fun onClickPop(state: State) {
        if (!state.isClosedAllerDialog)
            dispatch(Message.ChangeAllerDialogState(true))
        else
            dispatch(Message.ChangeAllerDialogState(false))
    }

    private fun StartStatement.updateAge(): StartStatement {
        return if (birthday.isNotEmpty()) {
//            val birthLocalDate =
//                LocalDate.parse(birthday, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        //    val year = ChronoUnit.YEARS.between(birthLocalDate, LocalDate.now()).toInt()
            copy(age = "year".toString())
        } else
            copy()

    }
}