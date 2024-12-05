package com.markettwits.start.register.presentation.registration.presentation.component.registration

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.members.member_common.domain.ProfileMember
import com.markettwits.start.register.domain.StartStatement
import com.markettwits.start.register.presentation.registration.presentation.component.StartStageComponent
import com.markettwits.start.register.presentation.registration.presentation.components.registration.StartRegistrationStagePage
import com.markettwits.start.register.presentation.registration.presentation.store.StartRegistrationPageStore
import kotlinx.coroutines.flow.StateFlow

interface StartRegistrationPageComponent {

    val pages : Value<ChildStack<*, StartStageComponent>>

    val state : StateFlow<StartRegistrationPageStore.State>

    fun obtainEvent(intent : StartRegistrationPageStore.Intent)

}