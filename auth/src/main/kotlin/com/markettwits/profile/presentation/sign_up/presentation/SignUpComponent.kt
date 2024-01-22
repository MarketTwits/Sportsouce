package com.markettwits.profile.presentation.sign_up.presentation

import com.arkivanov.decompose.ComponentContext
import com.markettwits.profile.presentation.sign_up.presentation.store.SignUpStore
import kotlinx.coroutines.flow.StateFlow

interface SignUpComponent {
    val state : StateFlow<SignUpStore.State>
    fun obtainEvent(intent : SignUpStore.Intent)
}