package com.markettwits.sportsouce.start.register.presentation.registration.member.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.sportsouce.start.register.domain.StartStatement
import com.markettwits.sportsouce.start.register.presentation.registration.member.domain.RegistrationMemberValidator
import com.markettwits.sportsouce.start.register.presentation.registration.member.store.RegistrationMemberStore.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


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

    @OptIn(ExperimentalTime::class)
    private fun StartStatement.updateAge(): StartStatement {
        return if (birthday.isNotEmpty()) {
            val birthLocalDate = try {
                val parts = birthday.split(".")
                LocalDate(parts[2].toInt(), parts[1].toInt(), parts[0].toInt()) // LocalDate(year, month, day)
            } catch (_: Exception) {
                return copy()
            }

            val currentDate = Clock.System.todayIn(TimeZone.currentSystemDefault())

            val years = currentDate.year - birthLocalDate.year - if (
                currentDate.month.number < birthLocalDate.month.number ||
                (currentDate.month.number == birthLocalDate.month.number && currentDate.day < birthLocalDate.day)
            ) 1 else 0

            copy(age = years.toString())
        } else {
            copy()
        }
    }
}