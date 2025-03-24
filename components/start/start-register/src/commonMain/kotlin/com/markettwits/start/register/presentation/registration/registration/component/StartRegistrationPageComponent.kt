package com.markettwits.start.register.presentation.registration.registration.component

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.start.register.presentation.registration.registration.store.StartRegistrationPageStore
import kotlinx.coroutines.flow.StateFlow

interface StartRegistrationPageComponent {

    val pages : Value<ChildStack<*, StartStageComponent>>

    val state : StateFlow<StartRegistrationPageStore.State>

    fun obtainEvent(intent : StartRegistrationPageStore.Intent)

}