package com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.component

import com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.store.SignInStore
import kotlinx.coroutines.flow.StateFlow

internal interface SignInScreen {
    val state: StateFlow<SignInStore.State>
    fun obtainEvent(intent: SignInStore.Intent)
    fun back()
    fun forgotPassword()
}