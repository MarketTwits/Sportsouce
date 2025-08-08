package com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.event.StateEventWithContent
import com.markettwits.core_ui.items.event.consumed
import com.markettwits.sportsouce.auth.flow.internal.sign_in.domain.LoginMethod
import com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.store.SignInStore.*

interface SignInStore : Store<Intent, State, Label> {

    data class State(
        val isLoading: Boolean = false,
        val emailOrPhone: String = "",
        val password: String = "",
        val smsCode: String = "",
        val enabled: Boolean = false,
        val emailOrPhoneError: String? = null,
        val passwordError: String? = null,
        val smsCodeError: String? = null,
        val loginMethod: LoginMethod = LoginMethod.PASSWORD,
        val isEmailOrPhoneFocused: Boolean = false,
        val isPasswordFocused: Boolean = false,
        val isSmsCodeFocused: Boolean = false,
        val isSmsCodeSent: Boolean = false,
        val smsCodeSending: Boolean = false,
        val event: StateEventWithContent<EventContent> = consumed(),
    )

    sealed interface Intent {
        data object Login : Intent
        data object SendSmsCode : Intent
        data class UpdateEmailOrPhone(val input: String) : Intent
        data class UpdatePassword(val password: String) : Intent
        data class UpdateSmsCode(val smsCode: String) : Intent
        data class SetLoginMethod(val method: LoginMethod) : Intent
        data class SetEmailOrPhoneFocus(val focused: Boolean) : Intent
        data class SetPasswordFocus(val focused: Boolean) : Intent
        data class SetSmsCodeFocus(val focused: Boolean) : Intent
        data object SignUp : Intent
        data object MessageHasBeenShowed : Intent
    }

    sealed interface Label {
        data object GoSignUp : Label
        data object GoProfile : Label
    }

    fun interface Factory {
        fun create(): SignInStore
    }
}