package com.markettwits.profile.internal.sign_up.presentation.component

import com.markettwits.profile.internal.sign_up.presentation.store.SignUpStore
import kotlinx.coroutines.flow.StateFlow

interface SignUpComponent {
    val state : StateFlow<SignUpStore.State>
    fun obtainEvent(intent : SignUpStore.Intent)
}