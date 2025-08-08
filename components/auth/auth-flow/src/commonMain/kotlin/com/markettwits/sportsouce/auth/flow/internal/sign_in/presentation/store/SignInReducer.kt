package com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.event.consumed
import com.markettwits.core_ui.items.event.triggered
import com.markettwits.sportsouce.auth.flow.internal.sign_in.domain.LoginMethod
import com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.store.SignInStore.State

object SignInReducer : Reducer<State, SignInReducer.Message> {

    sealed interface Message {
        data object Loading : Message
        data object Success : Message
        data class Error(val message: String) : Message
        data class UpdateEmailOrPhone(val input: String) : Message
        data class UpdatePassword(val password: String) : Message
        data class UpdateSmsCode(val smsCode: String) : Message
        data class SetLoginMethod(val method: LoginMethod) : Message
        data class SetEmailOrPhoneFocus(val focused: Boolean) : Message
        data class SetPasswordFocus(val focused: Boolean) : Message
        data class SetSmsCodeFocus(val focused: Boolean) : Message
        data class SetFieldError(
            val emailOrPhoneError: String?,
            val passwordError: String?,
            val smsCodeError: String?,
        ) : Message

        data class UpdateButtonState(val enabled: Boolean) : Message
        data object SmsCodeSendingStarted : Message
        data object SmsCodeSent : Message
        data object SmsCodeSendingFailed : Message
        data object MessageConsumed : Message
    }

    override fun State.reduce(msg: Message): State =
        when (msg) {
            is Message.Loading -> copy(
                isLoading = true,
                event = consumed()
            )

            is Message.Success -> copy(
                isLoading = false,
                event = consumed()
            )

            is Message.Error -> copy(
                isLoading = false,
                event = triggered(EventContent(false, msg.message))
            )

            is Message.UpdateEmailOrPhone -> copy(
                emailOrPhone = msg.input,
                emailOrPhoneError = null // Clear error on input
            )

            is Message.UpdatePassword -> copy(
                password = msg.password,
                passwordError = null // Clear error on input
            )

            is Message.UpdateSmsCode -> copy(
                smsCode = msg.smsCode,
                smsCodeError = null // Clear error on input
            )

            is Message.SetLoginMethod -> copy(
                loginMethod = msg.method,
                // Clear errors when switching methods
                passwordError = null,
                smsCodeError = null,
                // Reset SMS state when switching away from SMS
                isSmsCodeSent = if (msg.method == LoginMethod.SMS) isSmsCodeSent else false
            )

            is Message.SetEmailOrPhoneFocus -> copy(
                isEmailOrPhoneFocused = msg.focused
            )

            is Message.SetPasswordFocus -> copy(
                isPasswordFocused = msg.focused
            )

            is Message.SetSmsCodeFocus -> copy(
                isSmsCodeFocused = msg.focused
            )

            is Message.SetFieldError -> copy(
                emailOrPhoneError = msg.emailOrPhoneError,
                passwordError = msg.passwordError,
                smsCodeError = msg.smsCodeError
            )

            is Message.UpdateButtonState -> copy(
                enabled = msg.enabled
            )

            is Message.SmsCodeSendingStarted -> copy(
                smsCodeSending = true
            )

            is Message.SmsCodeSent -> copy(
                smsCodeSending = false,
                isSmsCodeSent = true
            )

            is Message.SmsCodeSendingFailed -> copy(
                smsCodeSending = false
            )

            is Message.MessageConsumed -> copy(
                event = consumed()
            )
        }
}