package com.markettwits.sportsouce.auth.flow.internal.forgot_password.presentation.component

import com.markettwits.sportsouce.auth.flow.internal.forgot_password.presentation.store.ForgotPasswordStore
import kotlinx.coroutines.flow.StateFlow

interface ForgotPasswordComponent {
    val state: StateFlow<ForgotPasswordStore.State>
    fun obtainEvent(intent: ForgotPasswordStore.Intent)
}
