package com.markettwits.start.register.presentation.member.component

import com.markettwits.start.register.presentation.member.store.RegistrationMemberStore
import kotlinx.coroutines.flow.StateFlow

interface RegistrationMemberComponent {
    val model: StateFlow<RegistrationMemberStore.State>
    fun obtainEvent(event: RegistrationMemberStore.Intent)
}
